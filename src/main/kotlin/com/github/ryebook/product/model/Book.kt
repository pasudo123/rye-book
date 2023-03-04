package com.github.ryebook.product.model

import com.github.ryebook.common.error.DomainPolicyException
import com.github.ryebook.common.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "books")
class Book(
    name: String,
    author: String,
    publisher: String,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "name", length = 128, columnDefinition = "VARCHAR(128) comment '책이름'")
    var name: String = name
        protected set

    @Column(name = "author", length = 128, columnDefinition = "VARCHAR(128) comment '작가'")
    var author: String = author
        protected set

    @Column(name = "publisher", length = 128, columnDefinition = "VARCHAR(128) comment '출판사'")
    var publisher: String = publisher
        protected set

    @Column(name = "register", columnDefinition = "TINYINT(1) comment '상품등록여부'")
    var register: Boolean = false
        protected set

    init {
        this.initValidateOrThrow()
    }

    fun registered() {
        this.register = true
    }

    fun deregistered() {
        this.register = false
    }

    /**
     * 초기화 조건에 부합하지 않으면 익셉션이 발생합니다.
     * 부합한다면 그대로 리턴합니다.
     */
    private fun initValidateOrThrow() {
        if (name.isBlank() || author.isBlank() || publisher.isBlank()) {
            throw DomainPolicyException("도서 등록 시, 필요한 정보가 없습니다.")
        }
    }
}
