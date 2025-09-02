package com.giwankim.boardgamebuddy

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AskController(
    private val boardGameService: BoardGameService,
) {
    @PostMapping("/ask")
    fun ask(
        @RequestBody question: Question,
    ): Answer = boardGameService.askQuestion(question)
}
