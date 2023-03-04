package com.github.ryebook.book.model

import com.github.ryebook.common.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "book")
class Book(
    @Column(name = "name", length = 128, columnDefinition = "VARCHAR(128) comment='책이름'")
    private var name: String,
    @Column(name = "author", length = 128, columnDefinition = "VARCHAR(128) comment='작가'")
    private var author: String,
    @Column(name = "publisher", length = 128, columnDefinition = "VARCHAR(128) comment='출판사'")
    private var publisher: String
): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set
}