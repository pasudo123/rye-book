package com.github.ryebook.product.model

data class BookProduct(
    val id: Long,
    val name: String,
    val author: String,
    val publisher: String,
    val price: Long? = null
) {

    companion object {
        fun from(book: Book, price: Long): BookProduct {
            return BookProduct(
                book.id!!,
                book.name,
                book.author,
                book.publisher,
                price
            )
        }
    }
}
