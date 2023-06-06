package com.github.ryebook.booking.api

import khttp.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class BookingConcurrencyMultipleControllerTest {

    private val localhost = "http://localhost:8080"

    @Test
    fun `재고 n 개에 대해서 동시성 제어 확인 V1 (redis decr)`() {

        // given
        val userIds = (1..21).map {
            "이순신-$it"
        }

        runBlocking {
            userIds.map { userId ->
                async(Dispatchers.IO) {
                    val map = mapOf<String, Any>(
                        "userId" to userId,
                        "productId" to 1L
                    )
                    post(url = "$localhost/bookings-multi/pre-payment-v1", json = map)
                }
            }.awaitAll()
        }
    }
}
