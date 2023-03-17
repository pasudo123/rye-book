package com.github.ryebook.product.model.pub

import com.github.ryebook.common.error.DomainPolicyException

data class TicketProduct(
    val ticket: Ticket,
    val price: Long? = null
) {

    init {
        ticket.deregisterOrThrow()
    }

    companion object {
        fun from(ticket: Ticket, price: Long): TicketProduct {
            return TicketProduct(
                ticket,
                price
            )
        }
    }

    private fun Ticket.deregisterOrThrow() {
        if (this.register) {
            throw DomainPolicyException("bookId[${this.id}] 는 상품으로 등록되었습니다.")
        }
    }
}
