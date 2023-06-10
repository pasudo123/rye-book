package com.github.ryebook.common.infra

import com.github.ryebook.common.error.CustomException
import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.ProductV2
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.SessionCallback
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

/**
 * redis client(lettuce) + transaction(+ watch operation) 연산을 통한 락
 * - 단일 레디스 서버에 대한 동시성 보장
 *
 * https://redis.io/docs/manual/transactions/#optimistic-locking-using-check-and-set
 * https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#tx.spring
 */
@Service
class LettuceLockTemplate(
    private val stringRedisTemplate: StringRedisTemplate
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val INIT_LOCK_VALUE = 0
        private const val SOLD_OUT_COUNT = 0L
        private const val DOES_NOT_BOOKING_LOCK = -1L
    }

    /**
     * @return Pair<Boolean, Long>
     *     - 예약가능한지 여부
     *     - 재고갯수
     */
    fun doLockingMultipleWithIncrDecrOrFalse(
        userId: String,
        product: Product? = null,
        productV2: ProductV2? = null,
    ): Pair<Boolean, Long> {

        val productId = product?.id ?: productV2!!.id
        val productQuantity = product?.quantity ?: productV2!!.quantity
        val productKey = "product-multiple:$productId"
        val productStockHistoryKey = "product-multiple-history:$productId"
        val currentQuantity: Long

        try {
            stringRedisTemplate.opsForValue()
                .setIfAbsent(productKey, productQuantity.toString(), Duration.ofSeconds(60))
            currentQuantity = stringRedisTemplate.opsForValue().decrement(productKey) ?: -1L

            if (currentQuantity <= DOES_NOT_BOOKING_LOCK) {
                // decr 을 통해서 현재 재고에 대한 에약건을 계속 줄여나간다. -1 이 되기 전까지
                log.error("@@ user.id=$userId 는 더 이상 상품 product.id=$productId 을 구매할 수 없습니다.")
                stringRedisTemplate.opsForValue().setIfPresent(productKey, "$SOLD_OUT_COUNT", Duration.ofSeconds(60))
                return Pair(false, SOLD_OUT_COUNT)
            }
        } catch (exception: Exception) {
            // 예상치 못한 에러 발생 시, 일정시간 동안 재고를 0으로 변경하고 예약을 하지 못하도록 막는다. ? -> 좋은방법은 아닌듯..
            // FIXME : (1) 바로 아래처럼 set() 을 하면 재고가 n 개 이상 남았는데, 예약을 수행하지 못하는 경우가 발생한다.
            // log.error("@@ user.id=$userId, product.id=$productId : 예상치 못한 에러가 발생 : ${exception.message}")
            // stringRedisTemplate.opsForValue().set(productKey, "$SOLD_OUT_COUNT", Duration.ofSeconds(30))

            // FIXME : (2) 실패한 건에 대해서만 다시 incr 을 수행
            log.error("@@ user.id=$userId, product.id=$productId : 예상치 못한 에러가 발생 : ${exception.message} -> 재시도 요청하도록 유도")
            stringRedisTemplate.opsForValue().increment(productKey)
            return Pair(false, SOLD_OUT_COUNT)
        }

        val zRange = try {
            stringRedisTemplate.opsForZSet().add(productStockHistoryKey, "$currentQuantity", currentQuantity.toDouble())
            stringRedisTemplate.expire(productStockHistoryKey, Duration.ofSeconds(30))
            stringRedisTemplate.opsForZSet().range(productStockHistoryKey, 0L, 0L)
                ?: throw CustomException("key=$productKey 에 해당하는 score 를 찾을 수 없습니다.")
        } catch (exception: Exception) {
            log.error("zset operation error : ${exception.message}")
            emptySet<String>()
        }

        if (zRange.isNotEmpty()) {
            println("z-range : $zRange")
            return Pair(true, zRange.iterator().next().toLong())
        }

        return Pair(true, SOLD_OUT_COUNT)
    }

    /**
     * 단 하나의 자원에 대해서 락을 건다.
     * 상품의 재고가 6개라도 동시 요청이 3개가 오면 재고가 3으로 줄어드는게 아닌 재고는 5로 변경된다.
     */
    fun doLockingPossibleOrFalse(
        userId: String,
        product: Product? = null,
        productV2: ProductV2? = null,
    ): Boolean {

        val productId = if (product != null) product.id else productV2!!.id
        val productKey = "product:$productId"

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

            log.info("@@ txResults : $txResults")
            if (txResults.isEmpty()) {
                log.info("@@ user.id=$userId 는 예약할 수 없습니다. 누군가가 예약을 수행 중입니다.")
            }

            return txResults.isNotEmpty()
        } catch (exception: Exception) {
            log.error("@@ user.id=$userId, product.id=$productId : 예상치 못한 에러가 발생 : ${exception.message}")
            return false
        }
    }
}
