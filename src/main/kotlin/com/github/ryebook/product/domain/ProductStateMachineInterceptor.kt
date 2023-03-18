package com.github.ryebook.product.domain

import com.github.ryebook.product.domain.ProductEventHandleService.Companion.EMPTY_PRODUCT_ID
import com.github.ryebook.product.domain.ProductEventHandleService.Companion.PRODUCT_ID_HEADER
import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.state.State
import org.springframework.statemachine.support.StateMachineInterceptorAdapter
import org.springframework.statemachine.transition.Transition
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class ProductStateMachineInterceptor(
    private val productDomainGetService: ProductDomainGetService,
    private val productDomainCreateService: ProductDomainCreateService
) : StateMachineInterceptorAdapter<Product.Status, Product.Event>() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun preStateChange(
        state: State<Product.Status, Product.Event>?,
        message: Message<Product.Event>?,
        transition: Transition<Product.Status, Product.Event>?,
        stateMachine: StateMachine<Product.Status, Product.Event>?,
        rootStateMachine: StateMachine<Product.Status, Product.Event>?
    ) {
        val productId = message?.headers?.getOrDefault(PRODUCT_ID_HEADER, EMPTY_PRODUCT_ID).toString()
        val product = productDomainGetService.findByIdOrNull(productId.toLong()) ?: return
        product.changeStatus(state?.id)
        productDomainCreateService.create(product)
        log.info("@@@ preStateChange Completed : productId[$productId], to change state[${state?.id}]")
    }
}
