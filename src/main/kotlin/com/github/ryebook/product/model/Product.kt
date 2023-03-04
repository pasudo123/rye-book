package com.github.ryebook.product.model

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
    private val productType: ProductType,
    price: Long,
    @Column(name = "quantity", columnDefinition = "bigint comment '수량'")
    private var quantity: Long = 0
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "price", columnDefinition = "bigint comment '가격'")
    var price: Long = price
        protected set

    @JoinColumn(name = "book_id", nullable = true)
    @OneToOne(targetEntity = Book::class, fetch = FetchType.LAZY, optional = false)
    var book: Book? = null
        protected set

    enum class ProductType(desc: String) {
        BOOK("도서타입")
    }

    fun mapping(book: Book) {
        this.book = book
    }

    companion object {
        fun fromBook(bookProduct: BookProduct): Product {
            return Product(
                ProductType.BOOK,
                bookProduct.price!!,
                quantity = 0L,
            ).apply {
                this.mapping(bookProduct.book)
            }
        }
    }
}
