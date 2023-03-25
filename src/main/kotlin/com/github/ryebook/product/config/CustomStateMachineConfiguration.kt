package com.github.ryebook.product.config

import com.github.ryebook.product.domain.sm.action.ProductCustomActionErrorHandler
import com.github.ryebook.product.domain.sm.action.ProductCustomActionHandler
import com.github.ryebook.product.domain.sm.guard.ProductCustomGuard
import com.github.ryebook.product.model.pub.Product
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.action.Actions
import org.springframework.statemachine.config.EnableStateMachineFactory
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer
import java.util.EnumSet
import javax.annotation.PostConstruct

/**
 * https://docs.spring.io/spring-statemachine/docs/3.2.0/reference/#statemachine-examples-persist
 * https://docs.spring.io/spring-statemachine/docs/3.2.0/reference/
 */
/**
 * - contextEvents = false : 스프링 컨텍스트를 사용하지 않고, 별도 StateMachineListener 의 이벤트 방식을 이용
 * (https://docs.spring.io/spring-statemachine/docs/3.2.0/reference/#limitations-and-problems)
 */
@Configuration
@EnableStateMachineFactory(contextEvents = false)
class CustomStateMachineConfiguration(
    private val productCustomGuard: ProductCustomGuard,
    private val productCustomActionHandler: ProductCustomActionHandler,
    private val productCustomActionErrorHandler: ProductCustomActionErrorHandler,
) : EnumStateMachineConfigurerAdapter<Product.Status, Product.Event>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun init() {
        log.info(":: StateMachineConfiguration PostConstruct ::")
    }

    override fun configure(states: StateMachineStateConfigurer<Product.Status, Product.Event>) {
        states.withStates()
            // 초기화 시점
            .initial(Product.Status.NEW)
            .states(EnumSet.allOf(Product.Status::class.java))
    }

    /**
     * event 수행 시, source 랑 target 에 맞는 데이터가 있어야 한다. 그렇지 않으면 sendEvent() 메소드가 수행 안됨
     * ProductDomainEventHandler.kt 참고 : sendEvent() 내부에서 denied 처리가 된다.
     * AbstractStateMachine 클래스의 acceptEvent() 쪽에서 처리가 되는걸로 보임
     */
    override fun configure(transitionConfigurer: StateMachineTransitionConfigurer<Product.Status, Product.Event>) {
        transitionConfigurer
            .withExternal()
            .source(Product.Status.NEW)
            .target(Product.Status.TO_BE_SALE)
            .event(Product.Event.RECEIVING_CONFIRMED)
            .action(Actions.errorCallingAction(productCustomActionHandler, productCustomActionErrorHandler))
            .guard(productCustomGuard)
            .and()
            .withExternal()
            .source(Product.Status.TO_BE_SALE)
            .target(Product.Status.ON_SALE)
            .event(Product.Event.IN_STOCK)
            .action(Actions.errorCallingAction(productCustomActionHandler, productCustomActionErrorHandler))
            .guard(productCustomGuard)
            .and()
            .withExternal()
            .source(Product.Status.ON_SALE)
            .target(Product.Status.SALE_END)
            .event(Product.Event.SOLD_OUT)
            .action(Actions.errorCallingAction(productCustomActionHandler, productCustomActionErrorHandler))
            .guard(productCustomGuard)
            .and()
            .withExternal()
            .source(Product.Status.SALE_END)
            .target(Product.Status.ON_SALE)
            .event(Product.Event.IN_STOCK)
            .action(Actions.errorCallingAction(productCustomActionHandler, productCustomActionErrorHandler))
            .guard(productCustomGuard)
            .and()
            .withExternal()
            .source(Product.Status.SALE_END)
            .target(Product.Status.TO_BE_SALE)
            .event(Product.Event.RECEIVING_CONFIRMED)
            .action(Actions.errorCallingAction(productCustomActionHandler, productCustomActionErrorHandler))
            .guard(productCustomGuard)
    }

    override fun configure(config: StateMachineConfigurationConfigurer<Product.Status, Product.Event>) {
        config.withConfiguration()
            .autoStartup(true)
            .listener(ProductStateMachineListener())
    }
}
