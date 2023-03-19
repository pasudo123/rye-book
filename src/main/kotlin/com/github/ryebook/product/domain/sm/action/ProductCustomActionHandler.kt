package com.github.ryebook.product.domain.sm.action

import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.statemachine.StateContext
import org.springframework.statemachine.action.Action
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProductCustomActionHandler : Action<Product.Status, Product.Event> {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun execute(context: StateContext<Product.Status, Product.Event>?) {
        if (context.isChangeable().not()) {
            return
        }

        log.info("@@@ action : receive-event[${context!!.event}] {[${context.source?.id}] -> [${context.target?.id}]}")
    }

    private fun StateContext<Product.Status, Product.Event>?.isChangeable(): Boolean {
        if (this == null) {
            log.warn("@@@ action : stateContext is null")
            return false
        }

        if (this.source == null || this.target == null) {
            log.warn("@@@ action : source[${this.source}] or target[${this.target}] is null")
            return false
        }

        // 동일한 경우
        if (this.source == this.target) {
            log.warn("@@@ action : source[${this.source}] == target[${this.target}], equals")
            return false
        }

        return true
    }
}
