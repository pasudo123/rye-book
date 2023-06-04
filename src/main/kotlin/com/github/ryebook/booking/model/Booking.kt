package com.github.ryebook.booking.model

import com.github.ryebook.common.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table
@Entity(name = "booking")
class Booking(
    @Column(name = "user_id", nullable = false, columnDefinition = "VARCHAR(128) comment '유저아이디'")
    val userId: String,

    @Column(name = "product_id", nullable = false, columnDefinition = "BIGINT comment '상품아이디'")
    val productId: Long
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", columnDefinition = "VARCHAR(32) comment '예약상태'")
    var status: BookingStatus = BookingStatus.BOOKING

    enum class BookingStatus(desc: String) {
        BOOKING("예약접수"),
        CANCELED("취소완료"),
        COMPLETED("구매완료(=이용완료, ...)"),
    }
}
