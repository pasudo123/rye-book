package com.github.ryebook.product.application

import com.github.ryebook.product.domain.ProductDomainGetService
import com.github.ryebook.product.model.con.Merchandise
import com.github.ryebook.product.model.pub.Product
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductGetService(
    private val productDomainGetService: ProductDomainGetService
) {

    fun findProductsWithPageable(type: Product.Type, pageable: Pageable): List<Merchandise> {
        return productDomainGetService.findAllWithPageable(type, pageable)
    }
}
