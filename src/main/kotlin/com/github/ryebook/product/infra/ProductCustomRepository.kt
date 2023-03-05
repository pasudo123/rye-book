package com.github.ryebook.product.infra

import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.QBook.book
import com.github.ryebook.product.model.pub.QProduct.product
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ProductCustomRepository(
    private val queryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Product::class.java) {

    fun findBooksWithPageable(pageable: Pageable): List<Product> {

        return queryFactory.selectFrom(product)
            .innerJoin(product.book, book)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(product.id.asc())
            .fetchJoin()
            .fetch()
    }
}
