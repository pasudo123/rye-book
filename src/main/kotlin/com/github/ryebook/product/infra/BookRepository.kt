package com.github.ryebook.product.infra

import com.github.ryebook.product.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long> {

    fun findAllByRegister(register: Boolean): List<Book>

    fun findOneById(id: Long): Book?
}
