package com.github.ryebook.product.domain

import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.statemachine.listener.StateMachineListenerAdapter
import org.springframework.statemachine.state.State
import org.springframework.stereotype.Component

@Component
class ProductStateMachineListener : StateMachineListenerAdapter<Product.Status, Product.Event>() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun stateChanged(from: State<Product.Status, Product.Event>?, to: State<Product.Status, Product.Event>?) {
        log.info("@@@ stateChanged : $from -> $to")
        super.stateChanged(from, to)
    }
}
