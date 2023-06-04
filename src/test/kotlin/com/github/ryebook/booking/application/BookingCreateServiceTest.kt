package com.github.ryebook.booking.application

import com.github.ryebook.IntegrationTestSupport
import com.github.ryebook.product.application.ProductCreateService
import com.github.ryebook.product.application.ProductModifyService
import com.github.ryebook.product.infra.TicketRepository
import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.Ticket
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.test.context.TestPropertySource
import java.time.LocalDateTime

@IntegrationTestSupport
@TestPropertySource(
    properties = ["custom-config.init-data = false"]
)
class BookingCreateServiceTest(
    private val ticketRepository: TicketRepository,
    private val productCreateService: ProductCreateService,
    private val productModifyService: ProductModifyService,
    private val bookingCreateService: BookingCreateService,
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private lateinit var books: List<Product>
    private lateinit var tickets: List<Product>

    // 초기수량
    private val quantity = 3L

    @BeforeEach
    fun `데이터 초기화`() {
        val tickets = mutableListOf<Ticket>()
        tickets.add(
            Ticket(
                name = "2023 여름 피아노 콘서트",
                availableStartedAt = LocalDateTime.now(),
                availableEndedAt = LocalDateTime.now().plusDays(7)
            )
        )

        val ticketIds = ticketRepository.saveAllAndFlush(tickets).mapNotNull { it.id }
        val productIds = productCreateService.createProductWithTypeAndPrice(ticketIds, Product.Type.TICKET, price = 30000)
        productModifyService.modifyQuantity(productIds.first(), quantity)
    }

    @Test
    fun `특정 프로덕트를 구매한다`() {

        // given
        println(">")
        // when

        // then
    }
}
