package com.giwankim.boardgamebuddy

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.evaluation.RelevancyEvaluator
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.evaluation.EvaluationRequest
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service

@Service
class SelfEvaluatingBoardGameService(
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

    private val evaluator = RelevancyEvaluator(chatClientBuilder)

    @Retryable(retryFor = [AnswerNotRelevantException::class])
    override fun askQuestion(question: Question): Answer {
        val answerText = chatClient.prompt()
            .user(question.question)
            .call()
            .content()!!

        evaluateRelevancy(question, answerText)

        return Answer(answerText)
    }

    @Recover
    fun recover(e: AnswerNotRelevantException): Answer {
        return Answer("I'm sorry, I wasn't able to answer the question")
    }

    private fun evaluateRelevancy(question: Question, answerText: String) {
        val evaluationRequest = EvaluationRequest(question.question, answerText)
        val evaluationResponse = evaluator.evaluate(evaluationRequest)
        if (!evaluationResponse.isPass) {
            throw AnswerNotRelevantException(question.question, answerText)
        }
    }
}
