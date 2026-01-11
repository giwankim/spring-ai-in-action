package com.giwankim.boardgamebuddy

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class SpringAiBoardGameService(
    chatClientBuilder: ChatClient.Builder,
    @param:Value("classpath:/promptTemplates/questionPromptTemplate.st") private val questionPromptTemplate: Resource,
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
        val answerText =
            chatClient
                .prompt()
                .user {
                    it.text(questionPromptTemplate)
                        .param("gameTitle", question.gameTitle)
                        .param("question", question.question)
                }
                .call()
                .content()

        return Answer(question.gameTitle, answerText)
    }
}
