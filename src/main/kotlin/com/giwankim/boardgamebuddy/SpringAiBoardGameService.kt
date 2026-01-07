package com.giwankim.boardgamebuddy

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.stereotype.Service

@Service
class SpringAiBoardGameService(
    chatClientBuilder: ChatClient.Builder,
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
                .user(question.question)
                .call()
                .content() ?: ""
        return Answer(answerText)
    }
}
