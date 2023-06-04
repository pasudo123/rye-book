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

    /**
     * 재고가 1개이상 남았는지 여부 체크
     */
    private fun isBookingPossible(): Boolean {
        return this.product.quantity >= 1
    }
}
