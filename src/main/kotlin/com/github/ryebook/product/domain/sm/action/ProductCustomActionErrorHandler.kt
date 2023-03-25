package com.github.ryebook.product.domain.sm.action

import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.statemachine.StateContext
import org.springframework.statemachine.action.Action
import org.springframework.stereotype.Component

@Component
class ProductCustomActionErrorHandler : Action<Product.Status, Product.Event> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun execute(context: StateContext<Product.Status, Product.Event>?) {
        if (context == null) {
            log.error("@@@ action error handle : context is null")
            return
        }

        log.error("@@@ action error handle : current-state[${context.stateMachine?.state?.id}] / receive-event[${context.event}]")
    }
}
