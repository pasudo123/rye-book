package com.github.ryebook.common.config

import com.github.ryebook.product.model.pub.Product
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.config.EnableStateMachineFactory
import org.springframework.statemachine.config.StateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer
import org.springframework.statemachine.data.RepositoryState
import org.springframework.statemachine.data.RepositoryTransition
import org.springframework.statemachine.data.StateRepository
import org.springframework.statemachine.data.TransitionRepository
import java.util.EnumSet

/**
 * https://docs.spring.io/spring-statemachine/docs/3.2.0/reference/
 * https://www.youtube.com/watch?v=A-dVgRV5-Bw&ab_channel=SpringFrameworkGuru
 */
@Configuration
@EnableStateMachineFactory
class CustomStateMachineConfiguration(
    private val stateRepository: StateRepository<RepositoryState>,
    private val transitionRepository: TransitionRepository<RepositoryTransition>
): StateMachineConfigurerAdapter<Product.Status, Product.Event>() {

    override fun configure(states: StateMachineStateConfigurer<Product.Status, Product.Event>) {
        states.withStates()
            .initial(Product.Status.NEW)
            .states(EnumSet.allOf(Product.Status::class.java))
            .end(Product.Status.TO_BE_SALE)
            .end(Product.Status.ON_SALE)
            .end(Product.Status.SALE_END)
            .end(Product.Status.NO_LONGER_FOR_SALE)
    }

    override fun configure(transitionConfigurer: StateMachineTransitionConfigurer<Product.Status, Product.Event>) {
        transitionConfigurer.withExternal()
            .source(Product)
    }
}
