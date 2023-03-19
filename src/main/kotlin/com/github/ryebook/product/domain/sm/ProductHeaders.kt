package com.github.ryebook.product.domain.sm

import com.github.ryebook.common.error.DataNotFoundException
import org.springframework.messaging.MessageHeaders

object ProductHeaders {

    const val PRODUCT_ID_HEADER = "product_header_id"

    fun MessageHeaders?.getProductHeaderIdOrThrow(): Long {
        return this?.get(PRODUCT_ID_HEADER, Long::class.java) ?: throw DataNotFoundException("$PRODUCT_ID_HEADER 가 존재하지 않습니다.")
    }
}
