package com.giwankim.boardgamebuddy

fun interface BoardGameService {
    fun askQuestion(question: Question): Answer
}
