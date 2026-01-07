package com.giwankim.boardgamebuddy

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource
import org.wiremock.spring.ConfigureWireMock
import org.wiremock.spring.EnableWireMock
import java.nio.charset.Charset

@EnableWireMock(
    ConfigureWireMock(baseUrlProperties = ["openai.base.url"]),
)
@SpringBootTest(properties = ["spring.ai.openai.base-url=\${openai.base.url}"])
class SpringAiBoardGameServiceWireMockTests(
    @param:Value("classpath:/test-openai-response.json") val responseResource: Resource,
    val chatClientBuilder: ChatClient.Builder,
) {
    @BeforeEach
    fun setUp() {
        val cannedResponse = responseResource.getContentAsString(Charset.defaultCharset())
        val mapper = ObjectMapper()
        val responseNode = mapper.readTree(cannedResponse)
        WireMock.stubFor(
            WireMock.post("/v1/chat/completions")
                .willReturn(ResponseDefinitionBuilder.okForJson(responseNode)),
        )
    }

    @Test
    fun `ask question`() {
        val boardGameService = SpringAiBoardGameService(chatClientBuilder)
        val answer = boardGameService.askQuestion(Question("What is the capital of France?"))
        Assertions.assertThat(answer).isNotNull
        Assertions.assertThat(answer.answer).isEqualTo("Paris")
    }
}
