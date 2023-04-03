package com.github.ryebook.product.domain.sm

import com.github.ryebook.common.error.SystemStateChangeException
import com.github.ryebook.product.domain.ProductDomainGetService
import com.github.ryebook.product.domain.sm.ProductHeaders.getProductHeaderIdOrThrow
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
class ProductDomainEventInterceptor(
    private val productDomainGetService: ProductDomainGetService,
) : StateMachineInterceptorAdapter<Product.Status, Product.Event>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun preStateChange(
        state: State<Product.Status, Product.Event>?,
        message: Message<Product.Event>?,
        transition: Transition<Product.Status, Product.Event>?,
        stateMachine: StateMachine<Product.Status, Product.Event>?,
        rootStateMachine: StateMachine<Product.Status, Product.Event>?
    ) {

        if (state.isNull()) {
            log.info("@@@ preStateChanged : state is null")
            return
        }

        log.info("@@@ preStateChanged : state.id[${state!!.id}]")

        val productId = message?.headers?.getProductHeaderIdOrThrow() ?: return
        val currentProduct = productDomainGetService.findByIdOrNull(productId)?.apply {
            this.changeStatus(state.id)
        } ?: return

        // dirty checking
        // productDomainCreateService.createOrPatch(currentProduct)
    }

    private fun State<Product.Status, Product.Event>?.isNull(): Boolean {
        return this == null
    }

    override fun stateMachineError(
        stateMachine: StateMachine<Product.Status, Product.Event>?,
        exception: Exception?
    ): Exception {
        if (stateMachine == null || exception == null) {
            throw SystemStateChangeException("스테이트 머신에 문제가 발생했습니다. : state-machine[$stateMachine], exception[$exception]")
        }

        log.error("@@@ stateMachineError : state.id[${stateMachine.state.id}]")
        throw SystemStateChangeException("스테이트 머신에 문제가 발생 : ${exception.message}")
    }
}
