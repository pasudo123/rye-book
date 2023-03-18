package com.github.ryebook.product.domain

import com.github.ryebook.IntegrationTestSupport
import com.github.ryebook.product.model.pub.Product
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Pageable

@IntegrationTestSupport
@DisplayName("productEventHandleService 는")
class ProductEventHandleServiceTest(
    private val productDomainGetService: ProductDomainGetService,
    private val productEventHandleService: ProductEventHandleService,
) {

    private lateinit var books: List<Product>
    private lateinit var tickets: List<Product>

    @BeforeEach
    fun beforeEach() {
        this.books = productDomainGetService.findAllWithPageable(Product.Type.BOOK, Pageable.ofSize(10))
        this.tickets = productDomainGetService.findAllWithPageable(Product.Type.TICKET, Pageable.ofSize(10))
    }

    @Test
    fun `product 의 상태를 TO_BE_SALE 로 변경한다`() {
        val firstBook = books.first()
        firstBook.status shouldBe Product.Status.NEW

        // 입고에정 이벤트
        productEventHandleService.handleEvent(firstBook, Product.Event.RECEIVING_CONFIRMED)

        val actual = productDomainGetService.findByIdOrNull(firstBook.id!!)!!
        actual.status shouldBe Product.Status.TO_BE_SALE
    }
}
