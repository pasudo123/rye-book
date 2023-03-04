package com.github.ryebook.product.infra

import com.github.ryebook.product.model.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

    fun findAllByProductType(productType: Product.ProductType, pageable: Pageable): List<Product>
}