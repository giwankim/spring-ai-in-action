package com.giwankim.mcpfilesystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class McpFilesystemClientApplication

fun main(args: Array<String>) {
    runApplication<McpFilesystemClientApplication>(*args)
}
