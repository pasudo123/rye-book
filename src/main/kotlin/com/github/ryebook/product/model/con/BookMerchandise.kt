package com.github.ryebook.product.model.con

import com.github.ryebook.product.model.pub.Book
import com.github.ryebook.product.model.pub.Product

/**
 * product(공급) <-> merchandise(수요) 를 구분
 */
class BookMerchandise(
    val book: Book,
    val product: Product
) : Merchandise() {

    companion object {
        fun from(product: Product): BookMerchandise {
            return BookMerchandise(
                product.book!!,
                product
            )
        }
    }
}
