package com.github.ryebook.product.model.con

import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.Ticket

/**
 * 사용자 입장
 */
class TicketMerchandise(
    val ticket: Ticket,
    override val product: Product
) : Merchandise(product) {

    companion object {
        fun from(product: Product): TicketMerchandise {
            return TicketMerchandise(
                product.ticket!!,
                product
            )
        }
    }
}
