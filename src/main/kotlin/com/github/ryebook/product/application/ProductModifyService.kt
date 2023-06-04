package com.github.ryebook.product.application

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductModifyService(
    private val productGetService: ProductGetService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun modifyQuantity(
        productId: Long,
        quantity: Long
    ) {
        productGetService.findByIdOrThrow(productId).run { this.changeQuantity(quantity) }
        productGetService.findV2ByIdOrThrow(productId).run { this.changeQuantity(quantity) }
    }
}
