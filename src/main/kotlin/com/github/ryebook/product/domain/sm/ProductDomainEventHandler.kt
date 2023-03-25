package com.github.ryebook.product.domain.sm

import com.github.ryebook.product.domain.sm.ProductHeaders.PRODUCT_ID_HEADER
import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.messaging.support.MessageBuilder
import org.springframework.statemachine.config.StateMachineFactory
import org.springframework.statemachine.support.DefaultStateMachineContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ProductDomainEventHandler(
    private val productStateMachineFactory: StateMachineFactory<Product.Status, Product.Event>,
    private val productStateMachineInterceptor: ProductDomainEventInterceptor
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun sendEventWithProduct(product: Product, productEvent: Product.Event) {
        val stateMachine = productStateMachineFactory.getStateMachine(product.id.toString())

        stateMachine.run {
            this.stopReactively().doOnError {
                log.error("@@@ stopReactively().error : $it")
            }.subscribe()
            this.stateMachineAccessor.doWithAllRegions { access ->
                access.addStateMachineInterceptor(productStateMachineInterceptor)
                access.resetStateMachineReactively(
                    DefaultStateMachineContext(product.status, null, null, null)
                ).doOnError {
                    log.error("@@@ resetStateMachineReactively().error : $it")
                }.subscribe()
            }
            this.startReactively().doOnError {
                log.error("@@@ startReactively().error : $it")
            }.subscribe()
        }

        val message = MessageBuilder.withPayload(productEvent)
            .setHeader(PRODUCT_ID_HEADER, product.id)
            .build()

        stateMachine.sendEvent(Mono.just(message)).doOnError {
            log.error("@@@ sendEvent().doOnError : $it")
        }.doOnCancel {
            log.warn("@@@ sendEvent().doOnCancel")
        }.doOnComplete {
            log.info("@@@ sendEvent().doOnComplete")
        }.doOnNext { eventResult ->
            log.info("@@@ sendEvent().doOnNext : ${eventResult.resultType}")
        }.log().subscribe()
    }
}
