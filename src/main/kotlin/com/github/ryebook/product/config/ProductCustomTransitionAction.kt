package com.github.ryebook.product.config

import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.statemachine.StateContext
import org.springframework.statemachine.action.Action
import org.springframework.stereotype.Component

@Component
class ProductCustomTransitionAction : Action<Product.Status, Product.Event> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun execute(context: StateContext<Product.Status, Product.Event>?) {
        if (context == null) {
            return
        }

        log.info("@@@ transition-action : [${context.source?.id}] -> [${context.target?.id}] / receive-event[${context.event}]")
    }
}
