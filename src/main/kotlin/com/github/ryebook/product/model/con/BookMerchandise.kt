package com.github.ryebook.product.model.con

import com.github.ryebook.product.model.pub.Product

/**
 * product <-> merchandise 를 구분
 */
class BookMerchandise(
    val id: Long,
    val name: String,
    val author: String,
    val publisher: String,
    val price: Long,
    val quantity: Long,
) : Merchandise() {

    companion object {
        fun from(product: Product): BookMerchandise {
            return BookMerchandise(
                product.book!!.id!!,
                product.book!!.name,
                product.book!!.author,
                product.book!!.publisher,
                product.price,
                product.quantity
            )
        }
    }
}
