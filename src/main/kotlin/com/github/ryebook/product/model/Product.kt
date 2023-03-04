package com.github.ryebook.product.model

import com.github.ryebook.common.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Table
@Entity(name = "products")
class Product(
    @Column(name = "product_type", columnDefinition = "VARCHAR(64) comment '상품타입'")
    private val productType: ProductType,
    @Column(name = "price", columnDefinition = "bigint comment '가격'")
    private val price: Long,
    @Column(name = "quantity", columnDefinition = "bigint comment '수량'")
    private var quantity: Long = 0
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @OneToOne(targetEntity = Book::class, fetch = FetchType.LAZY)
    private var book: Book? = null

    enum class ProductType(desc: String) {
        BOOK("도서타입")
    }

    companion object {
        fun fromBook(bookProduct: BookProduct): Product {
            return Product(
                ProductType.BOOK,
                bookProduct.price!!,
                quantity = 0L,
            )
        }
    }
}
