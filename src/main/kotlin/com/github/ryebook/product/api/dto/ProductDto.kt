package com.github.ryebook.product.api.dto

import com.github.ryebook.product.api.dto.ProductDto.BookProductResponse.Companion.toBookResponse
import com.github.ryebook.product.api.dto.ProductDto.TicketProductResponse.Companion.toTicketResponse
import com.github.ryebook.product.model.con.BookMerchandise
import com.github.ryebook.product.model.con.Merchandise
import com.github.ryebook.product.model.con.TicketMerchandise
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

class ProductDto {

    data class RequestEvent(
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

    @Schema(name = "티켓상품 응답모델", title = "asdadsdassdas")
    data class TicketProductResponse(
        val id: Long,
        val name: String,
        @Schema(name = "이용가능 시작시간")
        val availableStartedAt: LocalDateTime,
        @Schema(name = "이용가능 종료시간")
        val availableEndedAt: LocalDateTime,
        @Schema(name = "비고사항")
        val remark: String,
        @Schema(name = "가격")
        val price: Long,
        @Schema(name = "판매상태")
        val status: String,
        @Schema(name = "수량")
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

    @Schema(name = "도서상품 응답모델")
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
