package com.github.ryebook.product.domain

import com.github.ryebook.product.model.pub.Product
import org.springframework.messaging.support.MessageBuilder
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.config.StateMachineFactory
import org.springframework.statemachine.support.DefaultStateMachineContext
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProductEventHandleService(
    private val productStateMachineFactory: StateMachineFactory<Product.Status, Product.Event>,
    private val productStateMachineInterceptor: ProductStateMachineInterceptor
) {

    companion object {
        const val PRODUCT_ID_HEADER = "product_header_id"
        const val EMPTY_PRODUCT_ID = -1L
    }

    fun handleEvent(product: Product, productEvent: Product.Event) {
        val stateMachine = product.buildStateMachine()
        stateMachine.sendEvent(product, productEvent)
    }

    private fun StateMachine<Product.Status, Product.Event>.sendEvent(product: Product, productEvent: Product.Event) {
        val message = MessageBuilder.withPayload(productEvent)
            .setHeader(PRODUCT_ID_HEADER, product.id)
            .build()

        this.sendEvent(Mono.just(message)).blockFirst()
    }

    private fun Product.buildStateMachine(): StateMachine<Product.Status, Product.Event> {
        val stateMachine = productStateMachineFactory.getStateMachine(this.id.toString())

        stateMachine.stopReactively().block()
        stateMachine.stateMachineAccessor.doWithAllRegions { access ->
            access.addStateMachineInterceptor(productStateMachineInterceptor)
            access.resetStateMachineReactively(
                DefaultStateMachineContext(this.status, null, null, null)
            ).block()
        }
        stateMachine.startReactively().block()

        return stateMachine
    }
}
