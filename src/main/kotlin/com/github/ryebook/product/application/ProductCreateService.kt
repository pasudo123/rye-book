package com.github.ryebook.product.application

import com.github.ryebook.product.domain.ProductDomainCreateService
import com.github.ryebook.product.infra.BookRepository
import com.github.ryebook.product.infra.TicketRepository
import com.github.ryebook.product.model.pub.BookProduct
import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.ProductType
import com.github.ryebook.product.model.pub.ProductV2
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
    fun createProductWithTypeAndPrice(ids: List<Long>, type: ProductType, price: Long): List<Long> {
        return when (type) {
            ProductType.BOOK -> {
                val books = bookRepository.findAllByRegister(false).filter { ids.contains(it.id) }
                val bookProducts = books.map { BookProduct.from(it, price) }

                val productIds = productDomainCreateService.createOrPatch(bookProducts.map { Product.fromBook(it) })
                val productIdV2s = productDomainCreateService.createOrPatchV2(bookProducts.map { ProductV2.fromBook(it) })

                books.forEach { it.registered() }
                (productIds + productIdV2s).toSet().toList()
            }
            ProductType.TICKET -> {
                val tickets = ticketRepository.findAllByRegister(false).filter { ids.contains(it.id) }
                val ticketProducts = tickets.map { TicketProduct.from(it, price) }

                val productIds = productDomainCreateService.createOrPatch(ticketProducts.map { Product.fromTicket(it) })
                val productIdV2s = productDomainCreateService.createOrPatchV2(ticketProducts.map { ProductV2.fromTicket(it) })

                tickets.forEach { it.registered() }
                (productIds + productIdV2s).toSet().toList()
            }
        }
    }
}
