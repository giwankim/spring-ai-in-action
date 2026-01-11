package com.giwankim.boardgamebuddy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.evaluation.RelevancyEvaluator
import org.springframework.ai.document.Document
import org.springframework.ai.evaluation.EvaluationRequest
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringAiBoardGameServiceTests(
    val boardGameService: BoardGameService,
    val chatClientBuilder: ChatClient.Builder,
) {
    @Test
    fun `evaluate relevancy`() {
        val userText = "How many pieces are there?"
        val game = "Checkers"
        val question = Question(game, userText)

        val answer = boardGameService.askQuestion(question)

        val relevancyEvaluator = RelevancyEvaluator(chatClientBuilder)
        val response = relevancyEvaluator.evaluate(
            EvaluationRequest(
                userText,
                emptyList<Document>(),
                answer.answer,
            ),
        )

        assertThat(response.isPass)
            .withFailMessage {
                """
                ========================================
                The answer "${answer.answer}"
                is not considered relevant to the question
                "$userText".
                ========================================
                """.trimIndent()
            }
            .isTrue
    }
}
