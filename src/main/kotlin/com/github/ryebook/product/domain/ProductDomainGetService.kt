package com.github.ryebook.product.domain

import com.github.ryebook.product.infra.ProductCustomRepository
import com.github.ryebook.product.model.con.BookMerchandise
import com.github.ryebook.product.model.con.Merchandise
import com.github.ryebook.product.model.con.TicketMerchandise
import com.github.ryebook.product.model.pub.Product
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductDomainGetService(
    private val productCustomRepository: ProductCustomRepository,
) {

    fun findAllWithPageable(type: Product.Type, pageable: Pageable): List<Merchandise> {
        return productCustomRepository.findTypeWithPageable(type, pageable).map { Merchandise.from(it) }
    }

    fun findByIdOrNull(id: Long): Merchandise? {
        val product = productCustomRepository.findByIdOrNull(id)
        product?.run {
            if (this.isBook()) {
                return BookMerchandise.from(this)
            }

            if (this.isTicket()) {
                return TicketMerchandise.from(this)
            }
        }

        return Merchandise.toNull()
    }
}
