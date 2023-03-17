package com.github.ryebook.product.application

import com.github.ryebook.product.domain.ProductDomainCreateService
import com.github.ryebook.product.infra.BookRepository
import com.github.ryebook.product.infra.TicketRepository
import com.github.ryebook.product.model.pub.BookProduct
import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.TicketProduct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductCreateService(
    private val bookRepository: BookRepository,
    private val ticketRepository: TicketRepository,
    private val productDomainCreateService: ProductDomainCreateService
) {

    /**
     * 가격을 가지고 특정한 타입을 판매상품으로 등록한다.
     * @param type      타입
     * @param price     가격
     */
    @Transactional
    fun createProductWithTypeAndPrice(type: Product.Type, price: Long) {
        when (type) {
            Product.Type.BOOK -> {
                val books = bookRepository.findAllByRegister(false)
                val bookProducts = books.map { BookProduct.from(it, price) }
                productDomainCreateService.create(bookProducts.map { Product.fromBook(it) })
                books.forEach { it.registered() }
            }
            Product.Type.TICKET -> {
                val tickets = ticketRepository.findAllByRegister(false)
                val ticketProducts = tickets.map { TicketProduct.from(it, price) }
                productDomainCreateService.create(ticketProducts.map { Product.fromTicket(it) })
                tickets.forEach { it.registered() }
            }
        }
    }
}
