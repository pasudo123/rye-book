package com.github.ryebook.product.model.con

import com.github.ryebook.product.model.pub.Product

/**
 * 이용자 입장에서 상품
 */
sealed class Merchandise(
    open val product: Product
) {
    companion object {
        fun from(product: Product): Merchandise {
            return when (product.type) {
                Product.Type.BOOK -> BookMerchandise.from(product)
                Product.Type.TICKET -> TicketMerchandise.from(product)
            }
        }

        fun toNull(): BookMerchandise? {
            return null
        }
    }
}
