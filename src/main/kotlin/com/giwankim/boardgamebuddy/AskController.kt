package com.giwankim.boardgamebuddy

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AskController(
    private val boardGameService: BoardGameService,
) {
    @PostMapping("/ask")
    fun ask(
        @RequestBody @Valid question: Question,
    ): Answer = boardGameService.askQuestion(question)
}
