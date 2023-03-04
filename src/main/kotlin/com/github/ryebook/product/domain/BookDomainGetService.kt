package com.github.ryebook.product.domain

import com.github.ryebook.product.infra.BookRepository
import com.github.ryebook.product.model.Book
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BookDomainGetService(
    private val bookRepository: BookRepository
) {

    fun getBooksByRegister(register: Boolean): List<Book> {
        return bookRepository.findAllByRegister(register)
    }
}
