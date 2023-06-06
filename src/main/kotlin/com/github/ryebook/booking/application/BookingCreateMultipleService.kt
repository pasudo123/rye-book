package com.github.ryebook.booking.application

import com.github.ryebook.booking.infra.BookingRepository
import com.github.ryebook.booking.model.Booking
import com.github.ryebook.common.infra.RedisCasTemplate
import com.github.ryebook.product.application.ProductGetService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookingCreateMultipleService(
    private val productGetService: ProductGetService,
    private val bookingRepository: BookingRepository,
    private val redisLockTemplate: RedisCasTemplate
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun createBookingByProductIdV1(
        userId: String,
        productId: Long,
    ) {
        val product = productGetService.findByIdOrThrow(productId)
        val lockResult = redisLockTemplate.doLockingMultipleWithIncrDecrOrFalse(userId, product = product)

        if (product.isBookingPossible() && lockResult.first) {
            log.info("multiple : userId[$userId], 남은재고[${lockResult.second}] 예약 OOOOO")
            // redis 에서 원자성을 보장해도 결과적으로 mysql 단에서도 재고 갯수도 원자성을 보존하기 위해 레디스 정렬된 zpop 의 결과를 이용한다.
            product.applyQuantity(lockResult.second)
            bookingRepository.save(Booking(userId, product.id!!))
        } else {
            log.error("multiple : userId[$userId], 남은재고[${lockResult.second}] 예약 XXXXX")
        }
    }
}
