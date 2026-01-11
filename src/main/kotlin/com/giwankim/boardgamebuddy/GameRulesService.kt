package com.giwankim.boardgamebuddy

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class GameRulesService {
    fun getRulesFor(gameName: String): String {
        val filename = "classpath:/gameRules/${gameName.lowercase().replace(" ", "_")}.txt"
        return runCatching {
            DefaultResourceLoader()
                .getResource(filename)
                .getContentAsString(Charsets.UTF_8)
        }.onFailure {
            logger.info { "No rules found for game: $gameName" }
        }.getOrDefault("")
    }
}
