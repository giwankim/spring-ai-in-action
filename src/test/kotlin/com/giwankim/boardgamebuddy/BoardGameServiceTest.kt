package com.giwankim.boardgamebuddy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator
import org.springframework.ai.chat.evaluation.RelevancyEvaluator
import org.springframework.ai.evaluation.EvaluationRequest
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BoardGameServiceTest(
    val boardGameService: BoardGameService,
    chatClientBuilder: ChatClient.Builder,
) {
    val relevancyEvaluator: RelevancyEvaluator = RelevancyEvaluator(chatClientBuilder)

    val factCheckingEvaluator: FactCheckingEvaluator = FactCheckingEvaluator(chatClientBuilder)

    @Test
    fun `evaluate relevancy`() {
        val userText = "Why is the sky blue?"
        val question = Question(userText)

        val answer = boardGameService.askQuestion(question)

        val evaluationRequest = EvaluationRequest(userText, answer.answer)
        val evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest)

        assertThat(evaluationResponse.isPass)
            .withFailMessage(
                """
                ========================================
                The answer "%s"
                is not considered relevant to the question
                "%s".
                ========================================
                """.trimIndent(),
                answer.answer,
                userText,
            ).isTrue
    }

    @Test
    fun `evaluate factual accuracy`() {
        val userText = "Why is the sky blue?"
        val question = Question(userText)
        val answer = boardGameService.askQuestion(question)
        val referenceAnswer =
            "The sky is blue because of that was the paint color that was on sale."

        val evaluationRequest = EvaluationRequest(userText, answer.answer)
        val evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest)

        assertThat(evaluationResponse.isPass)
            .withFailMessage(
                """
                ========================================
                The answer "%s"
                is not considered correct for the question
                "%s".
                ========================================
                """.trimIndent(),
                answer.answer,
                userText,
            ).isTrue
    }
}
