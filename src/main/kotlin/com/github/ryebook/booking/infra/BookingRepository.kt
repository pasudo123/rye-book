package com.github.ryebook.booking.infra

import com.github.ryebook.booking.model.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : JpaRepository<Booking, Long>
