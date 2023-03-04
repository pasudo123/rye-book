package com.github.ryebook

import com.github.ryebook.book.infra.BookRepository
import com.github.ryebook.book.model.Book
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.UUID

@Profile("default")
@Component
class LocalDateInitializer(
    private val bookRepository: BookRepository
) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments?) {
        log.info("@@ init")
        addBooks(30)
    }

    private fun addBooks(size: Int) {
        val books = mutableListOf<Book>()

        (0..size).forEach {
            books.add(Book(
                name = "홍길동전-${randomUUIDByLength(10)}",
                author = "허균-${randomUUIDByLength(10)}",
                publisher = "민음사-${randomUUIDByLength(10)}"
            ))
        }

        bookRepository.saveAllAndFlush(books)
    }

    private fun randomUUIDByLength(length: Int): String {
        return UUID.randomUUID().toString().substring(0, length)
    }
}