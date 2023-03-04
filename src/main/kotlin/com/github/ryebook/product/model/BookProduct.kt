package com.github.ryebook.product.model

import com.github.ryebook.common.error.DomainPolicyException

data class BookProduct(
    val book: Book,
    val price: Long? = null
) {

    companion object {
        fun from(product: Product): BookProduct {
            return BookProduct(
                product.book!!,
                product.price
            )
        }

        fun from(book: Book, price: Long): BookProduct {
            if (book.register) {
                throw DomainPolicyException("bookId[${book.id}] 는 상품으로 등록되었습니다.")
            }
            return BookProduct(
                book,
                price
            )
        }
    }
}
