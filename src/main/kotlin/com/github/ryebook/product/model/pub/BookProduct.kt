package com.github.ryebook.product.model.pub

import com.github.ryebook.common.error.DomainPolicyException

data class BookProduct(
    val book: Book,
    val price: Long? = null
) {

    init {
        this.initValidateOrThrow()
    }

    private fun initValidateOrThrow() {
        if (book.register) {
            throw DomainPolicyException("bookId[${book.id}] 는 상품으로 등록되었습니다.")
        }
    }

    companion object {
        fun from(book: Book, price: Long): BookProduct {
            return BookProduct(
                book,
                price
            )
        }
    }
}
