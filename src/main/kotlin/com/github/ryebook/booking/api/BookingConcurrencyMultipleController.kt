package com.github.ryebook.booking.api

import com.github.ryebook.booking.api.dto.BookingDto
import com.github.ryebook.booking.application.BookingCreateMultipleService
import com.github.ryebook.booking.application.BookingCreateOnlyOneService
import com.github.ryebook.common.infra.RedisCasTemplate
import com.github.ryebook.product.application.ProductGetService
import com.github.ryebook.product.infra.ProductRepository
import com.github.ryebook.product.infra.ProductV2Repository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("bookings-multi")
@Tag(name = "BookingMultipleController", description = "ryebook 에 등록된 상품을 사전예약 구매")
class BookingConcurrencyMultipleController(
    private val bookingCreateMultipleService: BookingCreateMultipleService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Operation(
        summary = "사용자는 특정 프로덕트를 사전예약 구매",
        description = "redis cas 이용, 동시요청 시 있는 재고는 빠진다."
    )
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PostMapping("pre-payment-v1")
    fun createBookingByProductIdV1(
        @RequestBody request: BookingDto.Request,
    ) {
        bookingCreateMultipleService.createBookingByProductIdV1(request.userId, request.productId!!)
    }
}