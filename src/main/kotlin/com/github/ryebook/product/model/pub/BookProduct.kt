package com.github.ryebook.product.model.pub

import com.github.ryebook.common.error.DomainPolicyException

data class BookProduct(
    val book: Book,
    val price: Long? = null
) {

    init {
        book.deregisterOrThrow()
    }

    companion object {
        fun from(book: Book, price: Long): BookProduct {
            return BookProduct(
                book,
                price
            )
        }
    }

    private fun Book.deregisterOrThrow() {
        if (this.register) {
            throw DomainPolicyException("bookId[${this.id}] 는 상품으로 등록되었습니다.")
        }
    }
}
