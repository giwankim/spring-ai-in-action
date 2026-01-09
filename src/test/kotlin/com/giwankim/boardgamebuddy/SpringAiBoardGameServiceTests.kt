package com.giwankim.boardgamebuddy

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator
import org.springframework.ai.chat.evaluation.RelevancyEvaluator
import org.springframework.ai.evaluation.EvaluationRequest
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringAiBoardGameServiceTests(
    val boardGameService: BoardGameService,
    val chatClientBuilder: ChatClient.Builder,
) {
    lateinit var relevancyEvaluator: RelevancyEvaluator

    lateinit var factCheckingEvaluator: FactCheckingEvaluator

    @BeforeEach
    fun setUp() {
        relevancyEvaluator = RelevancyEvaluator(chatClientBuilder)

        factCheckingEvaluator = FactCheckingEvaluator.builder(chatClientBuilder)
            .build()
    }

    @Test
    fun `evaluate relevancy`() {
        val userText = "Why is the sky blue?"
        val question = Question(userText)
        val answer = boardGameService.askQuestion(question)

        val evaluationRequest = EvaluationRequest(userText, answer.answer)

        val response = relevancyEvaluator.evaluate(evaluationRequest)

        Assertions.assertThat(response.isPass)
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

    @Test
    fun `evaluate factual accuracy`() {
        val userText = "Why is the sky blue?"
        val question = Question(userText)
        val answer = boardGameService.askQuestion(question)

        val evaluationRequest = EvaluationRequest(userText, answer.answer)

        val response = factCheckingEvaluator.evaluate(evaluationRequest)

        Assertions.assertThat(response.isPass)
            .withFailMessage {
                """
              ========================================
              The answer "${answer.answer}"
              is not considered correct for the question
              "$userText".
              ========================================
                """.trimIndent()
            }
            .isTrue
    }
}
