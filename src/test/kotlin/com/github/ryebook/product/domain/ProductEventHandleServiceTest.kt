package com.github.ryebook.product.domain

import com.github.ryebook.IntegrationTestSupport
import com.github.ryebook.product.domain.sm.ProductDomainEventService
import com.github.ryebook.product.model.pub.Product
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable

@IntegrationTestSupport
@DisplayName("productEventHandleService 는")
class ProductEventHandleServiceTest(
    private val productDomainGetService: ProductDomainGetService,
    private val productEventHandleService: ProductDomainEventService,
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private lateinit var books: List<Product>
    private lateinit var tickets: List<Product>

    @BeforeEach
    fun beforeEach() {
        this.books = productDomainGetService.findAllWithPageable(Product.Type.BOOK, Pageable.ofSize(10))
        this.tickets = productDomainGetService.findAllWithPageable(Product.Type.TICKET, Pageable.ofSize(10))
        log.info("============================================================")
    }

    @Test
    fun `product 의 상태를 TO_BE_SALE 로 변경한다`() {
        val firstBook = books.first()
        firstBook.status shouldBe Product.Status.NEW

        // 입고예정 이벤트
        productEventHandleService.handleEvent(firstBook, Product.Event.RECEIVING_CONFIRMED)

        val actual = productDomainGetService.findByIdOrNull(firstBook.id!!)!!
        actual.status shouldBe Product.Status.TO_BE_SALE
    }

    /**
     * StateMachineConfiguration 에서 https://docs.spring.io/spring-statemachine/docs/3.2.0/reference/#statemachine-config-states 내용참고
     * initial, states, end 를 잘 써야하는듯.
     */
    @Test
    fun `product 의 상태를 ON_SALE 로 변경한다`() {
        val firstBook = books.last()
        firstBook.status shouldBe Product.Status.NEW

        // 입고예정 이벤트
        productEventHandleService.handleEvent(firstBook, Product.Event.RECEIVING_CONFIRMED)
        val currentProduct = productDomainGetService.findByIdOrNull(firstBook.id!!)!!
        currentProduct.status shouldBe Product.Status.TO_BE_SALE

        println("==> 판매예정 변경완료 ==>==>")

        // 재고존재 이벤트

        productEventHandleService.handleEvent(currentProduct, Product.Event.IN_STOCK)
        productDomainGetService.findByIdOrNull(currentProduct.id!!)!!.status shouldBe Product.Status.ON_SALE

        println("==> 판매중 변경완료 ==>==>")
    }
}
