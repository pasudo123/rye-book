package com.github.ryebook.product.api

import com.github.ryebook.product.application.ProductGetService
import com.github.ryebook.product.model.con.Merchandise
import com.github.ryebook.product.model.pub.Product
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("products")
class ProductController(
    private val productGetService: ProductGetService
) {

    @GetMapping("{type}")
    fun getProductsByType(
        @PathVariable("type") type: String,
        pageable: Pageable
    ): List<Merchandise> {
        return productGetService.findProductsWithPageable(Product.Type.valueOf(type.uppercase()), pageable)
    }
}
