package com.github.ryebook.booking.api

import com.github.ryebook.booking.api.dto.BookingDto
import com.github.ryebook.booking.application.BookingCreateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("bookings")
@Tag(name = "BookingController", description = "ryebook 에 등록된 상품을 사전예약 구매")
class BookingController(
    private val bookingCreateService: BookingCreateService
) {

    @Operation(summary = "사용자는 특정 프로덕트를 사전예약 구매", description = "ProductId 를 기반으로 특정한 상품에 대해 사전예약을 진행한다.")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PostMapping("pre-payment")
    fun createBookingByProductId(
        @RequestBody request: BookingDto.Request
    ) {
        bookingCreateService.createBookingByProductId(request.userId, request.productId!!)
    }
}
