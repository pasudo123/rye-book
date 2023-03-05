package com.github.ryebook.product.api

import com.github.ryebook.product.api.dto.ProductDto
import com.github.ryebook.product.api.dto.ProductDto.BookProductResponse.Companion.toResponses
import com.github.ryebook.product.application.ProductGetService
import com.github.ryebook.product.application.ProductPatchService
import com.github.ryebook.product.model.pub.Product
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("products")
class ProductController(
    private val productGetService: ProductGetService,
    private val productPatchService: ProductPatchService
) {

    @GetMapping("{type}")
    fun getProductsByType(
        @PathVariable("type") type: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        pageable: Pageable
    ): List<ProductDto.Response> {
        return productGetService
            .findProductsWithPageable(Product.Type.valueOf(type.uppercase()), pageable)
            .toResponses()
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PatchMapping("status")
    fun patchProductStatus(
        @RequestBody request: ProductDto.RequestWithStatus
    ) {
        productPatchService.patchStatus(request)
    }
}
