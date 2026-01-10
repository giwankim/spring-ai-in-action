package com.giwankim.boardgamebuddy

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.util.StreamUtils
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient
import org.stringtemplate.v4.ST
import java.nio.charset.Charset

@RestClientTest(SpringAiBoardGameService::class)
class SpringAiBoardGameServiceMockOpenAiTests(
    val mockServer: MockRestServiceServer,
    val boardGameService: SpringAiBoardGameService,
) {
    @TestConfiguration
    class TestConfig {
        @Bean
        fun chatClientBuilder(
            restClientBuilder: RestClient.Builder,
            webClientBuilder: WebClient.Builder,
        ): ChatClient.Builder {
            val openAiChatModel = OpenAiChatModel.builder()
                .openAiApi(
                    OpenAiApi.builder()
                        .baseUrl("https://api.openai.com")
                        .apiKey("TEST_API_KEY")
                        .restClientBuilder(restClientBuilder)
                        .webClientBuilder(webClientBuilder)
                        .build(),
                )
                .build()
            return ChatClient.builder(openAiChatModel)
        }
    }

    @Test
    fun `test stuff`() {
        val expectedAnswer = "Checkers is a game for two players."
        mockOpenAiChatResponse(expectedAnswer)
        val answer = boardGameService.askQuestion(Question("How many can play checkers?"))
        Assertions.assertThat(answer.answer)
            .isEqualTo(expectedAnswer)
    }

    private fun mockOpenAiChatResponse(content: String) {
        val responseResource = ClassPathResource("/response.json")
        val st = ST(
            StreamUtils.copyToString(responseResource.inputStream, Charset.defaultCharset()),
            '$',
            '$',
        ).add("content", content)

        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
            .andRespond(withSuccess(st.render(), MediaType.APPLICATION_JSON))
    }
}
