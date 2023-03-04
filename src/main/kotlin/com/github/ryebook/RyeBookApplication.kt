package com.github.ryebook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RyeBookApplication

fun main(args: Array<String>) {
    runApplication<RyeBookApplication>(*args)
}
