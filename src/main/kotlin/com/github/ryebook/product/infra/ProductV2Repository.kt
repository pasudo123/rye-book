package com.github.ryebook.product.infra

import com.github.ryebook.product.model.pub.ProductV2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductV2Repository : JpaRepository<ProductV2, Long>
