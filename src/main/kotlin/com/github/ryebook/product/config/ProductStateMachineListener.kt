package com.github.ryebook.product.config

import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.listener.StateMachineListenerAdapter
import org.springframework.statemachine.state.State

class ProductStateMachineListener : StateMachineListenerAdapter<Product.Status, Product.Event>() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun stateEntered(state: State<Product.Status, Product.Event>?) {
        log.info("@@@ stateEntered : state.id[${state?.id}]")
    }

    override fun stateChanged(from: State<Product.Status, Product.Event>?, to: State<Product.Status, Product.Event>?) {
        log.info("@@@ stateChanged : fromState.id[${from?.id}] -> toState.id[${to?.id}]")
    }

    override fun stateExited(state: State<Product.Status, Product.Event>?) {
        log.info("@@@ stateExited : state.id[${state?.id}]")
    }

    override fun stateMachineStopped(stateMachine: StateMachine<Product.Status, Product.Event>?) {
        log.warn("@@@ stateMachineStopped")
    }

    override fun stateMachineError(stateMachine: StateMachine<Product.Status, Product.Event>?, exception: Exception?) {
        log.error("@@@ stateMachineError : state.id[${stateMachine?.state?.id}], exception[${exception?.message}]")
    }
}
