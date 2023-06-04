package com.github.ryebook.product.application

import com.github.ryebook.common.error.DataNotFoundException
import com.github.ryebook.product.domain.ProductDomainGetService
import com.github.ryebook.product.model.con.Merchandise
import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.ProductType
import com.github.ryebook.product.model.pub.ProductV2
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductGetService(
    private val productDomainGetService: ProductDomainGetService
) {

    fun findMerchandisesWithPageable(type: ProductType, pageable: Pageable): List<Merchandise> {
        return productDomainGetService.findAllWithPageable(type, pageable).map { Merchandise.from(it) }
    }

    fun findMerchandiseByIdOrThrow(id: Long): Merchandise {
        val product = productDomainGetService.findByIdOrNull(id) ?: throw DataNotFoundException("product.id=$id 는 존재하지 않습니다.")
        return Merchandise.from(product)
    }

    fun findByIdOrThrow(id: Long): Product {
        return productDomainGetService.findByIdOrNull(id) ?: throw DataNotFoundException("product.id=$id 는 존재하지 않습니다.")
    }

    fun findV2ByIdOrThrow(id: Long): ProductV2 {
        return productDomainGetService.findV2ByIdOrNull(id) ?: throw DataNotFoundException("productV2.id=$id 는 존재하지 않습니다.")
    }
}
