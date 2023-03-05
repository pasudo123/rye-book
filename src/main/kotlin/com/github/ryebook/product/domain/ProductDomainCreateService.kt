package com.github.ryebook.product.domain

import com.github.ryebook.product.infra.ProductRepository
import com.github.ryebook.product.model.pub.BookProduct
import com.github.ryebook.product.model.pub.Product
import org.springframework.stereotype.Service

@Service
class ProductDomainCreateService(
    private val productRepository: ProductRepository
) {

    fun create(bookProducts: List<BookProduct>) {
        val products = bookProducts.map { Product.fromBook(it) }
        productRepository.saveAll(products)
    }
}
