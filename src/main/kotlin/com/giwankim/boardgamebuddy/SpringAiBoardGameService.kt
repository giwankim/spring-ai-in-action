package com.giwankim.boardgamebuddy

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.stereotype.Service

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
        val prompt = "Answer this question about ${question.gameTitle}: ${question.question}"
        val answerText =
            chatClient
                .prompt()
                .user(prompt)
                .call()
                .content() ?: ""
        return Answer(gameTitle = question.gameTitle, answer = answerText)
    }
}
