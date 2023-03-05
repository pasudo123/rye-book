package com.github.ryebook.product.model.con

/**
 * 이용자 입장에서 상품
 */
sealed class Merchandise {
    fun toBook(): BookMerchandise {
        return this as BookMerchandise
    }
}
