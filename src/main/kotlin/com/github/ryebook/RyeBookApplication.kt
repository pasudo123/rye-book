package com.github.ryebook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan(basePackages = ["com.github.ryebook"])
@EnableJpaRepositories(basePackages = ["com.github.ryebook"])
class RyeBookApplication

fun main(args: Array<String>) {
    runApplication<RyeBookApplication>(*args)
}
