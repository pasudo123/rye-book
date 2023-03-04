package com.github.ryebook.product.application

import com.github.ryebook.product.domain.ProductDomainGetService
import com.github.ryebook.product.model.BookProduct
import com.github.ryebook.product.model.Product
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductGetService(
    private val productDomainGetService: ProductDomainGetService
) {

    fun findBookProductWithPageable(productType: Product.ProductType, pageable: Pageable): List<BookProduct> {
        return productDomainGetService.findAllWithPageable(Product.ProductType.BOOK, pageable).map { currentProduct ->
            BookProduct.from(currentProduct)
        }
    }
}