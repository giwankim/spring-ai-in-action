package com.giwankim.boardgamebuddy

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

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

        val answerText =
            chatClient
                .prompt()
                .system {
                    it.text(promptTemplate)
                        .param("gameTitle", question.gameTitle)
                        .param("rules", gameRules)
                }
                .user(question.question)
                .call()
                .content()

        return Answer(gameTitle = question.gameTitle, answer = answerText)
    }
}
