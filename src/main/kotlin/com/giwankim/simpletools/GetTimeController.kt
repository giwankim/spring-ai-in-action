package com.giwankim.simpletools

import org.springframework.ai.chat.client.ChatClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.View

@RestController
class GetTimeController(
    chatClientBuilder: ChatClient.Builder,
    timeTools: TimeTools,
    private val error: View,
) {
    private val chatClient = chatClientBuilder.defaultTools(timeTools).build()

    @GetMapping("/time")
    fun getTime(@RequestParam city: String): String {
        return chatClient.prompt()
            .user { it.text(CURRENT_TIME_TEMPLATE).param("city", city) }
            .call()
            .content() ?: error("AI returned no content")
    }

    companion object {
        const val CURRENT_TIME_TEMPLATE = "What is the current time in {city}?"
    }
}
