package com.github.ryebook.product.api.dto

import com.github.ryebook.product.api.dto.ProductDto.BookProductResponse.Companion.toBookResponse
import com.github.ryebook.product.api.dto.ProductDto.TicketProductResponse.Companion.toTicketResponse
import com.github.ryebook.product.model.con.BookMerchandise
import com.github.ryebook.product.model.con.Merchandise
import com.github.ryebook.product.model.con.TicketMerchandise
import java.time.LocalDateTime

class ProductDto {

    data class RequestWithStatus(
        val id: Long,
        val status: String
    )

    data class RequestEvent(
        val id: Long,
        val event: String
    )

    sealed class Response {
        companion object {
            fun List<Merchandise>.toResponses(): List<Response> {
                return this.map { it.toResponse() }
            }

            fun Merchandise.toResponse(): Response {
                return when (this) {
                    is BookMerchandise -> {
                        this.toBookResponse()
                    }
                    is TicketMerchandise -> {
                        this.toTicketResponse()
                    }
                }
            }
        }
    }

    data class TicketProductResponse(
        val id: Long,
        val name: String,
        val availableStartedAt: LocalDateTime,
        val availableEndedAt: LocalDateTime,
        val remark: String,
        val price: Long,
        val status: String,
        val quantity: Long,
    ) : Response() {

        companion object {
            fun TicketMerchandise.toTicketResponse(): TicketProductResponse {
                return TicketProductResponse(
                    this.product.id!!,
                    this.ticket.name,
                    this.ticket.availableStartedAt,
                    this.ticket.availableEndedAt,
                    this.ticket.remark ?: "",
                    this.product.price,
                    this.product.status.name,
                    this.product.quantity
                )
            }
        }
    }

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
            fun BookMerchandise.toBookResponse(): BookProductResponse {
                return BookProductResponse(
                    this.product.id!!,
                    this.book.name,
                    this.book.author,
                    this.book.publisher,
                    this.product.price,
                    this.product.status.name,
                    this.product.quantity,
                )
            }
        }
    }
}
