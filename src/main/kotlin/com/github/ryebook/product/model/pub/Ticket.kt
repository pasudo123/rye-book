package com.github.ryebook.product.model.pub

import com.fasterxml.jackson.annotation.JsonFormat
import com.github.ryebook.common.model.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tickets")
class Ticket(
    name: String,
    availableStartedAt: LocalDateTime,
    availableEndedAt: LocalDateTime,
    remark: String? = null
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "name", nullable = false, length = 128, columnDefinition = "VARCHAR(128) comment '티켓명'")
    var name: String = name
        protected set

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "available_started_at", nullable = false, length = 128, columnDefinition = "datetime comment '이용가능 시작시간'")
    var availableStartedAt: LocalDateTime = availableStartedAt
        protected set

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "available_ended_at", nullable = false, length = 128, columnDefinition = "datetime comment '이용가능 종료시간'")
    var availableEndedAt: LocalDateTime = availableEndedAt
        protected set

    @Column(name = "remark", columnDefinition = "VARCHAR(256) comment '추가내용'")
    var remark: String? = remark

    @Column(name = "register", columnDefinition = "TINYINT(1) comment '상품등록여부'")
    var register: Boolean = false
        protected set

    fun registered() {
        this.register = true
    }

    fun deregistered() {
        this.register = false
    }
}
