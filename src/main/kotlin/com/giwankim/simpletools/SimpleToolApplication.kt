package com.giwankim.simpletools

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleToolApplication

fun main(args: Array<String>) {
    runApplication<SimpleToolApplication>(*args)
}
