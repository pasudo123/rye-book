package com.github.ryebook.product.model.con

import com.github.ryebook.product.model.pub.Product

/**
 * 이용자 입장에서 상품
 */
sealed class Merchandise {
    fun toBook(): BookMerchandise {
        return this as BookMerchandise
    }

    fun toTicket(): TicketMerchandise {
        return this as TicketMerchandise
    }

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
