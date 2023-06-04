package com.github.ryebook

import com.github.ryebook.common.util.isDataInit
import com.github.ryebook.product.application.ProductCreateService
import com.github.ryebook.product.application.ProductModifyService
import com.github.ryebook.product.infra.BookRepository
import com.github.ryebook.product.infra.TicketRepository
import com.github.ryebook.product.model.pub.Book
import com.github.ryebook.product.model.pub.ProductType
import com.github.ryebook.product.model.pub.Ticket
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Profile(value = ["default", "test"])
@Component
class LocalDateInitializer(
    private val environment: Environment,
    private val bookRepository: BookRepository,
    private val ticketRepository: TicketRepository,
    private val productCreateService: ProductCreateService,
    private val productModifyService: ProductModifyService,
) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments?) {

        if (environment.isDataInit().not()) {
            log.info("@@ init(XXX) @@")
            return
        }

        log.info("@@ init(OOO) @@")

        // addBooks()
        addTickets()
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

        val bookIds = bookRepository.saveAllAndFlush(books).mapNotNull { it.id }
        productCreateService.createProductWithTypeAndPrice(bookIds, ProductType.BOOK, price = 10000)
    }

    private fun addTickets() {
        val tickets = mutableListOf<Ticket>()

        tickets.add(
            Ticket(
                name = "2023 아이유 콘서트-${randomUUIDByLength(10)}",
                availableStartedAt = LocalDateTime.of(2023, 10, 1, 18, 0, 0),
                availableEndedAt = LocalDateTime.of(2023, 10, 1, 22, 0, 0)
            )
        )

        val ticketIds = ticketRepository.saveAllAndFlush(tickets).mapNotNull { it.id }
        val productIds = productCreateService.createProductWithTypeAndPrice(ticketIds, ProductType.TICKET, price = 30000)

        productIds.forEach { productId ->
            productModifyService.modifyQuantity(productId, quantity = 6)
        }
    }

    private fun randomUUIDByLength(length: Int): String {
        return UUID.randomUUID().toString().substring(0, length)
    }
}
