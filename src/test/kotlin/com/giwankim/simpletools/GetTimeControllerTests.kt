package com.giwankim.simpletools

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.evaluation.RelevancyEvaluator
import org.springframework.ai.document.Document
import org.springframework.ai.evaluation.EvaluationRequest
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GetTimeControllerTests(
    private val getTimeController: GetTimeController,
    private val chatClientBuilder: ChatClient.Builder,
    private val timeTools: TimeTools,
) {
    @Test
    fun `evaluate relevancy`() {
        chatClientBuilder.defaultTools(timeTools)
        val relevancyEvaluator = RelevancyEvaluator(chatClientBuilder)
        val userText = "What is the current time in Dallas?"

        val time = getTimeController.getTime("Seoul")

        val response =
            relevancyEvaluator.evaluate(EvaluationRequest(userText, emptyList<Document>(), time))
        Assertions.assertThat(response.isPass)
            .withFailMessage(
                """
          ========================================
          The answer "$time"
          is not considered relevant to the question
          "$userText".
          ========================================
                """.trimIndent(),
            )
    }
}
