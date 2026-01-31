package com.giwankim.simpletools

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.time.ZoneId
import java.time.ZonedDateTime

private val logger = KotlinLogging.logger {}

@Component
class TimeTools {
    @Tool(name = "getCurrentTime", description = "Get the current time in the specified timezone.")
    fun getCurrentTime(timeZone: String): String {
        logger.info { "Getting the current time in $timeZone" }
        val now = ZonedDateTime.now(ZoneId.of(timeZone))
        return now.toString()
    }
}
