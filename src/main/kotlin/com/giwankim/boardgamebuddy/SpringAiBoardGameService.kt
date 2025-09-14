package com.giwankim.boardgamebuddy

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Primary
@Service
class SpringAiBoardGameService(
    chatClientBuilder: ChatClient.Builder,
) : BoardGameService {
    private val chatClient: ChatClient =
        chatClientBuilder
            .apply {
                val chatOptions =
                    ChatOptions
                        .builder()
                        .model("gpt-5-nano")
                        .temperature(1.0)
                        .build()
                defaultOptions(chatOptions)
            }.build()

    override fun askQuestion(question: Question): Answer {
        val answerText =
            chatClient
                .prompt()
                .user(question.question)
                .call()
                .content()
        return Answer(answerText)
    }
}
