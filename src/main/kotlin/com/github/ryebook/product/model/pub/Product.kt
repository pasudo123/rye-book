package com.github.ryebook.product.model.pub

import com.github.ryebook.common.model.BaseEntity
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
class Product(
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", columnDefinition = "VARCHAR(64) comment '상품타입'")
    private val type: Type,
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
    @OneToOne(targetEntity = Book::class, fetch = FetchType.LAZY, optional = false)
    var book: Book? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", columnDefinition = "VARCHAR(64) comment '상품상태'")
    var status: Status = Status.OUT_OF_STOCK
        protected set

    enum class Type(desc: String) {
        BOOK("도서")
    }

    enum class Status(desc: String) {
        RECEIVING_SCHEDULED("입고예정"),
        UN_AVAILABLE("입고예정없음"),
        IN_STOCK("재고있음"),
        OUT_OF_STOCK("재고없음")
    }

    fun mapping(book: Book) {
        this.book = book
    }

    fun update(status: Product.Status) {
        this.status = status
    }

    companion object {
        fun fromBook(bookProduct: BookProduct): Product {
            return Product(
                Type.BOOK,
                bookProduct.price!!,
                quantity = 0L,
            ).apply {
                this.mapping(bookProduct.book)
            }
        }
    }
}
