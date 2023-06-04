package com.github.ryebook.booking.model

import com.github.ryebook.common.model.BaseEntity
import com.github.ryebook.product.model.pub.Product
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table
@Entity(name = "booking")
class Booking(
    @Column(name = "user_id", nullable = false, columnDefinition = "VARCHAR(128) comment '유저아이디'")
    val userId: String,

    @JoinColumn(name = "product_id", nullable = false)
    @OneToOne(targetEntity = Product::class, fetch = FetchType.EAGER, optional = false)
    val product: Product
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
