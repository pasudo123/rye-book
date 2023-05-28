package com.github.ryebook.common.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.ryebook.common.config.CustomMapperConfiguration.Companion.mapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CustomMapperConfiguration {

    companion object {
        val mapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT)
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return mapper
    }
}

inline fun <reified T : Any> T.object2Json(): String = mapper.writeValueAsString(this)
inline fun <reified T : Any> String.string2Object(): T = mapper.readValue(this, object : TypeReference<T>() {})
