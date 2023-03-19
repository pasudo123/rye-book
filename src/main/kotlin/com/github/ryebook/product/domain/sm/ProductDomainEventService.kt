package com.github.ryebook.product.domain.sm

import com.github.ryebook.product.model.pub.Product
import org.springframework.stereotype.Service

@Service
class ProductDomainEventService(
    private val productPersistHandler: ProductDomainEventHandler
) {

    fun handleEvent(product: Product, productEvent: Product.Event) {
        productPersistHandler.sendEventWithProduct(product, productEvent)
    }
}
