package com.github.ryebook.common.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
class CustomQuerydslConfiguration(
    @PersistenceContext
    private val entityManager: EntityManager
) {

    @Bean
    fun queryFactory(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}
