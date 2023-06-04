package com.github.ryebook.booking.application

import com.github.ryebook.IntegrationTestSupport
import com.github.ryebook.product.application.ProductCreateService
import com.github.ryebook.product.application.ProductGetService
import com.github.ryebook.product.application.ProductModifyService
import com.github.ryebook.product.infra.TicketRepository
import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.test.context.TestPropertySource

@IntegrationTestSupport
@TestPropertySource(
    properties = [
        "custom-config.init-data = false",
    ]
)
@Deprecated("미사용")
class BookingCreateServiceTest(
    private val ticketRepository: TicketRepository,
    private val productCreateService: ProductCreateService,
    private val productModifyService: ProductModifyService,
    private val productGetService: ProductGetService,
    private val bookingCreateService: BookingCreateService,
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private lateinit var product: Product

//    @Test
//    fun `특정 프로덕트를 구매한다`() = runBlocking {
//        initDataBeforeAsync()
//
//        val size = 100
//        val userIds = (1..size).map {
//            "홍길동-$it"
//        }
//
//        // given
//        userIds.map {
//            async(Dispatchers.IO) {
//                productGetService.findByIdOrThrow(product.id!!)
//            }
//            // bookingCreateService.createBookingByProductId(it, product.id!!)
//        }.awaitAll()
//
//        val currentProduct = productGetService.findByIdOrThrow(product.id!!)
//
//        log.info("전체유저수=$size, product.name=${currentProduct.name()}, product.quantity=${currentProduct.quantity}")
//        log.info("########################################################")
//    }
}
