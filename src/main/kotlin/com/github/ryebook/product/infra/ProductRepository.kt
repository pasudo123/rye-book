package com.github.ryebook.product.infra

import com.github.ryebook.product.model.pub.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long>
