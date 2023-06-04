package com.github.ryebook.booking.application

import com.github.ryebook.booking.infra.BookingRepository
import com.github.ryebook.product.application.ProductGetService
import com.github.ryebook.product.infra.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookingCreateService(
    private val productGetService: ProductGetService,
    private val productRepository: ProductRepository,
    private val bookingRepository: BookingRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 상품을 구매한다.
     * - 재고에 따른 동시성 제어를 수행한다. (with lock)
     */
    fun createBookingByProductId(
        userId: String,
        productId: Long
    ) {
        val merchandise = productGetService.findMerchandiseByIdOrThrow(productId)
        log.info("Hello")
    }
}
