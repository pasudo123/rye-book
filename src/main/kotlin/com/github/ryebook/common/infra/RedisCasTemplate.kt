package com.github.ryebook.common.infra

import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.SessionCallback
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

/**
 * https://redis.io/docs/manual/transactions/#optimistic-locking-using-check-and-set
 * https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#tx.spring
 */
@Service
class RedisCasTemplate(
    private val stringRedisTemplate: StringRedisTemplate
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val INIT_LOCK_VALUE = 0
    }

    fun doLockingPossibleOrFalse(userId: String, product: Product): Boolean {

        val productKey = "product:${product.id}"

        try {
            val txResults = stringRedisTemplate.execute(object : SessionCallback<List<Any>> {
                @JvmName("executeWithRedisOperations")
                fun execute(operations: RedisOperations<String, String>): List<Any> {

                    operations.watch(productKey)
                    val productLockValue = operations.opsForValue().get(productKey)?.toInt() ?: 0

                    if (productLockValue != INIT_LOCK_VALUE) {
                        // 초기 0 값이 아니면, 누군가 점유를 한 상태이다.
                        // 이후에 사용자가 결제실패 및 취소를 바로했더라도 재 예약할 수 없다. (ttl 60초를 기다려야 함)
                        return emptyList()
                    }

                    operations.multi()
                    // operations.opsForValue().set(productKey, "${INIT_LOCK_VALUE.plus(1)}", Duration.ofSeconds(60L))
                    operations.opsForValue().increment(productKey)
                    operations.expire(productKey, Duration.ofSeconds(60L))
                    return operations.exec()
                }

                @Suppress("UNCHECKED_CAST")
                override fun <K : Any?, V : Any?> execute(operations: RedisOperations<K, V>): List<Any> {
                    return this.execute(operations as RedisOperations<String, String>)
                }
            })

            if (txResults.isEmpty()) {
                log.info("@@ user.id=$userId 는 예약할 수 없습니다. 누군가가 예약을 수행 중입니다.")
            }

            return txResults.isNotEmpty()
        } catch (exception: Exception) {
            log.error("@@ user.id=$userId product.id=${product.id} 예상치 못한 에러가 발생 : ${exception.message}")
            return false
        }
    }
}
