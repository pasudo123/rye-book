package com.github.ryebook.product.api.dto

import com.github.ryebook.product.model.con.Merchandise

class ProductDto {

    data class RequestWithStatus(
        val id: Long,
        val status: String
    )

    sealed class Response

    data class BookProductResponse(
        val id: Long,
        val name: String,
        val author: String,
        val publisher: String,
        val price: Long,
        val status: String,
        val quantity: Long,
    ) : Response() {

        companion object {
            fun List<Merchandise>.toResponses(): List<BookProductResponse> {
                return this.map { merchandise ->

                    val bookMerchandise = merchandise.toBook()

                    BookProductResponse(
                        bookMerchandise.book.id!!,
                        bookMerchandise.book.name,
                        bookMerchandise.book.author,
                        bookMerchandise.book.publisher,
                        bookMerchandise.product.price,
                        bookMerchandise.product.status.name,
                        bookMerchandise.product.quantity,
                    )
                }
            }
        }
    }
}
