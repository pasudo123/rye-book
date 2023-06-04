package com.github.ryebook.booking.api

import khttp.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class BookingControllerTest {

    private val localhost = "http://localhost:8080"
    private val SIZE = 200

    @Test
    fun `api 호출을 통해서 동시성 제어를 확인한다 V1`() {

        val userIds = (1..SIZE).map {
            "홍길동-$it"
        }

        runBlocking {
            userIds.map { userId ->
                async(Dispatchers.IO) {

                    val map = mapOf<String, Any>(
                        "userId" to userId,
                        "productId" to 1L
                    )
                    post(url = "$localhost/bookings/pre-payment-v1", json = map)
                }
            }.awaitAll()
        }
    }

    @Test
    fun `api 호출을 통해서 동시성 제어를 확인한다 V2`() {

        val userIds = (1..SIZE).map {
            "홍길동-$it"
        }

        runBlocking {
            userIds.map { userId ->
                async(Dispatchers.IO) {

                    val map = mapOf<String, Any>(
                        "userId" to userId,
                        "productId" to 1L
                    )
                    post(url = "$localhost/bookings/pre-payment-v2", json = map)
                }
            }.awaitAll()
        }
    }
}
