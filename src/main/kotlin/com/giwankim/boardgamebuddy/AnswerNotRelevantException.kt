package com.giwankim.boardgamebuddy

class AnswerNotRelevantException(question: String, answer: String) : RuntimeException("The answer '$answer' is not relevant to the question '$question'.")
