package com.github.ryebook.product.api

import com.github.ryebook.product.api.dto.ProductDto
import com.github.ryebook.product.api.dto.ProductDto.Response.Companion.toResponse
import com.github.ryebook.product.api.dto.ProductDto.Response.Companion.toResponses
import com.github.ryebook.product.application.ProductGetService
import com.github.ryebook.product.domain.sm.ProductDomainEventHandler
import com.github.ryebook.product.model.pub.Product
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "ProductController", description = "ryebook 에 등록된 상품 컨트롤러")
class ProductController(
    private val productGetService: ProductGetService,
    private val productDomainEventHandler: ProductDomainEventHandler
) {

    @Operation(summary = "상품타입에 맞는 상품 페이징 조회", description = "상품 목록을 조회합니다.")
    @Parameters(
        value = [
            Parameter(name = "type", description = "상품타입", `in` = ParameterIn.QUERY),
        ]
    )
    @GetMapping
    fun getProductsByType(
        @RequestParam("type") type: String,
        pageable: Pageable
    ): List<ProductDto.Response> {
        return productGetService
            .findProductsWithPageable(Product.Type.valueOf(type.uppercase()), pageable)
            .toResponses()
    }

    @Operation(summary = "상품 Id 에 맞는 상품 단일 조회", description = "단일 상품을 조회합니다.")
    @GetMapping("{id}")
    fun getProductById(
        @PathVariable("id") id: Long
    ): ProductDto.Response {
        return productGetService.findByIdOrThrow(id).toResponse()
    }

    @Operation(summary = "상품 Id 에 대한 이벤트 발행", description = "특정 상품에 대한 이벤트를 발생시킵니다.")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PatchMapping("{id}/event")
    fun handleEvent(
        @PathVariable("id") id: Long,
        @RequestBody request: ProductDto.RequestEvent
    ) {
        val merchandise = productGetService.findByIdOrThrow(id)
        productDomainEventHandler.sendEventWithProduct(merchandise.product, Product.Event.valueOf(request.event))
    }
}
