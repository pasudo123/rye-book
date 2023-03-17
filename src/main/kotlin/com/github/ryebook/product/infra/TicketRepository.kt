package com.github.ryebook.product.infra

import com.github.ryebook.product.model.pub.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Long> {

    fun findAllByRegister(register: Boolean): List<Ticket>

    fun findOneById(id: Long): Ticket?
}
