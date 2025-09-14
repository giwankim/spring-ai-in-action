package com.giwankim.boardgamebuddy

import jakarta.validation.constraints.NotBlank

data class Question(
    @NotBlank(message = "Game title is required") val gameTitle: String,
    @NotBlank(message = "Question is required") val question: String,
)
