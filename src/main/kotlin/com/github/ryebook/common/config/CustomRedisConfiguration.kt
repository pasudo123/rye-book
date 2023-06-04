package com.github.ryebook.common.config

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class CustomRedisConfiguration(
    private val redisProperties: RedisProperties
) {

    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory(redisProperties.host, redisProperties.port)
    }

    @Bean
    fun stringRedisTemplate(): StringRedisTemplate {
        val connectionFactory = lettuceConnectionFactory().apply {
            this.database = 0
            this.afterPropertiesSet()
        }

        return StringRedisTemplate().apply {
            this.setConnectionFactory(connectionFactory)
        }
    }
}
