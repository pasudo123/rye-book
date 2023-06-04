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

    /**
     * LocalDateInitializer.Kt 에서 데이터초기화가 수행됨.
     * 따라서 아래구문에서는 조회만 하고 있음.
     */
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
     * initial, states, end 를 잘 써야하는듯.
     * event 발생 시, source 와 target 조건에 부합해야 한다.
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

//    @Test
//    fun `product 의 상태값을 TO_BE_SALE 에서 TO_BE_SALE 로 변경 시 익셉션이 발생한다`() {
//
//        val firstBook = books.last()
//        firstBook.status shouldBe Product.Status.NEW
//        productEventHandleService.handleEvent(firstBook, Product.Event.RECEIVING_CONFIRMED)
//        val currentProduct = productDomainGetService.findByIdOrNull(firstBook.id!!)!!
//
//        // when
//        val actual  = shouldThrow<SystemStateChangeException> {
//            log.info("@@@ shouldThrow =================================================")
//            productEventHandleService.handleEvent(currentProduct, Product.Event.RECEIVING_CONFIRMED)
//        }
//
//        // then
//        actual.message shouldContain "요청 상태값"
//    }
}
