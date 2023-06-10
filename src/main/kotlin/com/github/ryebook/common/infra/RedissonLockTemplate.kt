package com.github.ryebook.common.infra

import com.github.ryebook.product.model.pub.Product
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

/**
 * redisson 을 통한 락
 * - 단일 레디스 서버에 대한 동시성 보장
 * - 레디스 클러스터 구성 : distributed lock 기능도 제공
 *
 * https://github.com/redisson/redisson
 */
@Service
class RedissonLockTemplate(
    private val redissonClient: RedissonClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun doLockingPossibleOrFalse(
        userId: String,
        product: Product
    ): Boolean {
        val lockKey = "product:${product.id}"

        val currentProductLock: RLock = redissonClient.getLock(lockKey)

        return try {
            currentProductLock.tryLock(50, (6000 * 10 * 60), TimeUnit.MILLISECONDS)
        } catch (exception: Exception) {
            log.error("##userId=$userId redisson lock 획득 시 문제가 발생했습니다. : ${exception.message}")
            false
        }
    }
}
