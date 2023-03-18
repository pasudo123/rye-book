package com.github.ryebook.product.application

import com.github.ryebook.common.error.DataNotFoundException
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
        return productDomainGetService.findAllWithPageable(type, pageable).map { Merchandise.from(it) }
    }

    fun findByIdOrThrow(id: Long): Merchandise {
        val product = productDomainGetService.findByIdOrNull(id) ?: throw DataNotFoundException("product[$id] 는 존재하지 않습니다.")
        return Merchandise.from(product)
    }
}
