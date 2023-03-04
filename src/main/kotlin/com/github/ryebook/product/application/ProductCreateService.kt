package com.github.ryebook.product.application

import com.github.ryebook.product.domain.BookDomainGetService
import com.github.ryebook.product.domain.ProductDomainCreateService
import com.github.ryebook.product.model.BookProduct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductCreateService(
    private val bookDomainGetService: BookDomainGetService,
    private val productDomainCreateService: ProductDomainCreateService
) {

    /**
     * 가격을 가지고 책을 판매상품으로 등록한다.
     * @param price 가격
     */
    @Transactional
    fun createBookProductWithPrice(price: Long) {
        val books = bookDomainGetService.getBooksByRegister(false)
        val bookProducts = books.map { BookProduct.from(it, price) }
        productDomainCreateService.create(bookProducts)
        books.forEach { it.register() }
    }
}
