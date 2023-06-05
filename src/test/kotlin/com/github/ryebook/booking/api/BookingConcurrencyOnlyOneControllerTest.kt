package com.github.ryebook.booking.api

import khttp.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class BookingConcurrencyOnlyOneControllerTest {

    private val localhost = "http://localhost:8080"
    private val SIZE = 200

    @Test
    fun `동시성 제어를 확인한다 V1 (일반)`() {

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
    fun `동시성 제어를 확인한다 V2 (Mysql 낙관적락 사용)`() {

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

    @Test
    fun `동시성 제어를 확인한다 V3 (Redis 낙관적락 사용)`() {

        val userIds = (500..1000).map {
            "홍길동-$it"
        }

        runBlocking {
            userIds.map { userId ->
                async(Dispatchers.IO) {

                    val map = mapOf<String, Any>(
                        "userId" to userId,
                        "productId" to 1L
                    )
                    post(url = "$localhost/bookings/pre-payment-v3", json = map)
                }
            }.awaitAll()
        }
    }

    @Test
    fun `동시성 제어를 확인한다 V4 (Mysql + Redis 락사용)`() {

        val userIds = (1..5000).map {
            "홍길동-$it"
        }

        runBlocking {
            userIds.map { userId ->
                async(Dispatchers.IO) {

                    val map = mapOf<String, Any>(
                        "userId" to userId,
                        "productId" to 1L
                    )
                    post(url = "$localhost/bookings/pre-payment-v4", json = map)
                }
            }.awaitAll()
        }
    }
}
