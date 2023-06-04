package com.github.ryebook.product.model.pub

import com.github.ryebook.common.model.BaseEntity
import org.hibernate.annotations.SelectBeforeUpdate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table
@Entity(name = "products")
@SelectBeforeUpdate
class ProductV2(
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", columnDefinition = "VARCHAR(64) comment '상품타입'")
    val type: ProductType,
    price: Long,
    quantity: Long,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "price", columnDefinition = "bigint comment '가격'")
    var price: Long = price
        protected set

    @Column(name = "quantity", columnDefinition = "bigint comment '수량'")
    var quantity: Long = quantity
        protected set

    @JoinColumn(name = "book_id", nullable = true)
    @OneToOne(targetEntity = Book::class, fetch = FetchType.LAZY, optional = true)
    var book: Book? = null
        protected set

    @JoinColumn(name = "ticket_id", nullable = true)
    @OneToOne(targetEntity = Ticket::class, fetch = FetchType.LAZY, optional = true)
    var ticket: Ticket? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", columnDefinition = "VARCHAR(64) comment '상품상태'")
    var status: Status = Status.NEW
        protected set

    enum class Status(desc: String) {
        NEW("신규상태"),
        TO_BE_SALE("판매예정"),
        ON_SALE("판매중"),
        SALE_END("판매종료"),
    }

    enum class Event(desc: String) {
        RECEIVING_CONFIRMED("입고확정"),
        IN_STOCK("재고존재"),
        SOLD_OUT("품절"),
    }

    fun mapping(book: Book) {
        this.book = book
    }

    fun mapping(ticket: Ticket) {
        this.ticket = ticket
    }

    fun changeStatus(status: Status? = null) {
        status?.let { this.status = status }
    }

    fun changeQuantity(quantity: Long) {
        this.quantity = quantity
    }

    /**
     * 재고감소
     * 기본으로 1씩 감소
     */
    fun reduceQuantity(quantity: Long = -1) {
        this.quantity += quantity
    }

    /**
     * 재고가 1이상 남으면 예약이 가능하다
     * 그렇지 않으면 불가하다
     */
    fun isBookingPossible(): Boolean {
        return this.quantity >= 1
    }

    fun name() = if (this.type == ProductType.BOOK) {
        book!!.name
    } else {
        ticket!!.name
    }

    companion object {
        fun fromBook(bookProduct: BookProduct): ProductV2 {
            return ProductV2(
                ProductType.BOOK,
                bookProduct.price!!,
                quantity = 0L,
            ).apply {
                this.mapping(bookProduct.book)
            }
        }

        fun fromTicket(ticketProduct: TicketProduct): ProductV2 {
            return ProductV2(
                ProductType.TICKET,
                ticketProduct.price!!,
                quantity = 0L,
            ).apply {
                this.mapping(ticketProduct.ticket)
            }
        }
    }
}
