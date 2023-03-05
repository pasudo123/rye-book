package com.github.ryebook.product.application

import com.github.ryebook.common.error.DataNotFoundException
import com.github.ryebook.product.domain.ProductDomainCreateService
import com.github.ryebook.product.infra.BookRepository
import com.github.ryebook.product.model.pub.BookProduct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductCreateService(
    private val bookRepository: BookRepository,
    private val productDomainCreateService: ProductDomainCreateService
) {

    /**
     * 가격을 가지고 책을 판매상품으로 등록한다.
     * @param price     가격
     */
    @Transactional
    fun createBookProductWithPrice(price: Long) {
        val books = bookRepository.findAllByRegister(false)
        val bookProducts = books.map { BookProduct.from(it, price) }
        productDomainCreateService.create(bookProducts)
        books.forEach { it.registered() }
    }

    /**
     * @param bookId    책 ID
     * @param price     가격
     */
    @Transactional
    fun createBookProductWithIdAndPrice(bookId: Long, price: Long) {
        val book = bookRepository.findOneById(bookId) ?: throw DataNotFoundException("bookId[$bookId] 는 존재하지 않습니다.")
        val bookProduct = BookProduct.from(book, price)
        productDomainCreateService.create(listOf(bookProduct))
        book.registered()
    }
}
