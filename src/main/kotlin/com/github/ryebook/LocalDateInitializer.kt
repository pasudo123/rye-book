package com.github.ryebook

import com.github.ryebook.common.util.isDataInit
import com.github.ryebook.product.application.ProductCreateService
import com.github.ryebook.product.infra.BookRepository
import com.github.ryebook.product.model.pub.Book
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.util.UUID

@Profile("default")
@Component
class LocalDateInitializer(
    private val environment: Environment,
    private val bookRepository: BookRepository,
    private val productCreateService: ProductCreateService,
) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments?) {

        if (environment.isDataInit().not()) {
            log.info("@@ init(X) @@")
            return
        }

        log.info("@@ init(O) @@")
        addBooks()
        addBookProducts()
    }

    private fun addBooks() {
        val books = mutableListOf<Book>()

        books.add(
            Book(
                name = "홍길동전-${randomUUIDByLength(10)}",
                author = "허균-${randomUUIDByLength(10)}",
                publisher = "민음사-${randomUUIDByLength(10)}"
            )
        )

        books.add(
            Book(
                name = "구운몽전-${randomUUIDByLength(10)}",
                author = "김만중-${randomUUIDByLength(10)}",
                publisher = "민음사-${randomUUIDByLength(10)}"
            )
        )

        bookRepository.saveAllAndFlush(books)
    }

    private fun addBookProducts(price: Long = 10000) {
        productCreateService.createBookProductWithPrice(price)
    }

    private fun randomUUIDByLength(length: Int): String {
        return UUID.randomUUID().toString().substring(0, length)
    }
}
