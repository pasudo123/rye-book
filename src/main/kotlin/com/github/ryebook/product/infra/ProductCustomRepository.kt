package com.github.ryebook.product.infra

import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.ProductType
import com.github.ryebook.product.model.pub.QBook.book
import com.github.ryebook.product.model.pub.QProduct.product
import com.github.ryebook.product.model.pub.QTicket.ticket
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ProductCustomRepository(
    private val queryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Product::class.java) {

    fun findTypeWithPageable(type: ProductType, pageable: Pageable): List<Product> {

        val query = queryFactory.selectFrom(product)

        when (type) {
            ProductType.BOOK -> {
                query.innerJoin(product.book, book)
            }
            ProductType.TICKET -> {
                query.innerJoin(product.ticket, ticket)
            }
        }

        return query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(product.id.asc())
            .fetchJoin()
            .fetch()
    }

    fun findByIdOrNull(id: Long): Product? {
        return queryFactory.selectFrom(product)
            .where(product.id.eq(id))
            .leftJoin(product.book, book).fetchJoin()
            .leftJoin(product.ticket, ticket).fetchJoin()
            .fetchOne()
    }
}
