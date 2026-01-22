package com.giwankim.boardgamebuddy

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.entity
import org.springframework.ai.chat.client.responseEntity
import org.springframework.ai.chat.metadata.Usage
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger { }

@Service
class SpringAiBoardGameService(
    chatClientBuilder: ChatClient.Builder,
    @param:Value("classpath:/promptTemplates/systemPromptTemplate.st")
    private val promptTemplate: Resource,
    private val gameRulesService: GameRulesService,
) : BoardGameService {
    private val chatClient = chatClientBuilder
        .defaultOptions(
            ChatOptions.builder()
                .model("gpt-5-nano")
                .temperature(1.0)
                .build(),
        )
        .build()

    override fun askQuestion(question: Question): Answer {
        val gameRules = gameRulesService.getRulesFor(question.gameTitle)

        val responseEntity = chatClient
            .prompt()
            .system {
                it.text(promptTemplate)
                    .param("gameTitle", question.gameTitle)
                    .param("rules", gameRules)
            }
            .user(question.question)
            .call()
            .responseEntity<Answer>()

        val response = responseEntity.response
            ?: throw IllegalStateException("No response received from AI")

        response.metadata.usage?.let {
            logUsage(it)
        }

        return responseEntity.entity()
            ?: throw IllegalStateException("Failed to parse AI response")
    }

    private fun logUsage(usage: Usage) {
        logger.info { "Token usage: prompt=${usage.promptTokens}, generation=${usage.completionTokens}, total=${usage.totalTokens}" }
    }
}
