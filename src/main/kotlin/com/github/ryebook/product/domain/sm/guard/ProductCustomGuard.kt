package com.github.ryebook.product.domain.sm.guard

import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.statemachine.StateContext
import org.springframework.statemachine.guard.Guard
import org.springframework.stereotype.Component

@Component
class ProductCustomGuard : Guard<Product.Status, Product.Event> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun evaluate(context: StateContext<Product.Status, Product.Event>?): Boolean {
        log.info("@@@ guard")
        return true
    }
}
