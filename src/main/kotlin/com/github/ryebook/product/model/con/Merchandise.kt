package com.github.ryebook.product.model.con

import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.ProductType

/**
 * 이용자 입장에서 상품
 */
sealed class Merchandise(
    open val product: Product
) {
    companion object {
        fun from(product: Product): Merchandise {
            return when (product.type) {
                ProductType.BOOK -> BookMerchandise.from(product)
                ProductType.TICKET -> TicketMerchandise.from(product)
            }
        }

        fun toNull(): BookMerchandise? {
            return null
        }
    }
}
