package com.github.ryebook.booking.api

import com.github.ryebook.booking.api.dto.BookingDto
import com.github.ryebook.booking.application.BookingCreateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.hibernate.HibernateException
import org.slf4j.LoggerFactory
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
    private val bookingCreateService: BookingCreateService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Operation(
        summary = "사용자는 특정 프로덕트를 사전예약 구매",
        description = "mysql 단순 이용"
    )
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PostMapping("pre-payment-v1")
    fun createBookingByProductIdV1(
        @RequestBody request: BookingDto.Request,
    ) {
        bookingCreateService.createBookingByProductIdV1(request.userId, request.productId!!)
    }

    @Operation(
        description = "JPA 의 OptimisticLockType.VERSION 을 이용"
    )
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PostMapping("pre-payment-v2")
    fun createBookingByProductIdV2(
        @RequestBody request: BookingDto.Request,
    ) {
        try {
            bookingCreateService.createBookingByProductIdV2(request.userId, request.productId!!)
            log.info("userId[${request.userId}] 예약 OOOOO")
        } catch (exception: HibernateException) {
            log.error("[디비 익셉션] userId[${request.userId}] 예약 XXXXX : ${exception.message}")
        } catch (exception: Exception) {
            log.error("[일반 익셉션] userId[${request.userId}] 예약 XXXXX : ${exception.message}")
        }
    }
}