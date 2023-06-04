package com.github.ryebook.booking.application

import com.github.ryebook.booking.infra.BookingRepository
import com.github.ryebook.booking.model.Booking
import com.github.ryebook.product.application.ProductGetService
import com.github.ryebook.product.infra.ProductRepository
import com.github.ryebook.product.infra.ProductV2Repository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookingCreateService(
    private val productGetService: ProductGetService,
    private val productRepository: ProductRepository,
    private val productV2Repository: ProductV2Repository,

    private val bookingRepository: BookingRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun createBookingByProductIdV1(
        userId: String,
        productId: Long
    ) {
        val product = productGetService.findByIdOrThrow(productId)

        if (product.isBookingPossible()) {
            log.info("userId[$userId] 예약 OOOOO")
            product.reduceQuantity(quantity = -1)
            bookingRepository.save(Booking(userId, product.id!!))
            productRepository.save(product)
        } else {
            log.info("userId[$userId] 예약 XXXXX ")
        }
    }

    @Transactional
    fun createBookingByProductIdV2(
        userId: String,
        productId: Long
    ) {
        val product = productGetService.findV2ByIdOrThrow(productId)

        if (product.isBookingPossible()) {
            log.info("userId[$userId] 예약 OOOOO")
            product.reduceQuantity(quantity = -1)
            bookingRepository.save(Booking(userId, product.id!!))
            productV2Repository.save(product)
        } else {
            log.info("userId[$userId] 예약 XXXXX ")
        }
    }
}
