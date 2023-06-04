package com.github.ryebook.booking.api.dto

class BookingDto {

    abstract class BaseResource {
        lateinit var userId: String
        var productId: Long? = null
    }

    class Request : BaseResource()
}
