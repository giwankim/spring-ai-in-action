package com.giwankim.boardgamebuddy

import jakarta.validation.constraints.NotBlank

data class Question(
    @field:NotBlank(message = "Game title is required")
    val gameTitle: String,
    @field:NotBlank(message = "Question is required")
    val question: String,
)
