package com.github.ryebook.common.config

import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class CustomMapperConfigurationTest {

    @Test
    fun `mapper 로 클래스를 json 형태로 표시한다`() {

        // given
        val testPerson = TestPerson(
            "홍길동",
            30,
            LocalDate.of(2021, 10, 4),
            LocalDateTime.of(2021, 10, 4, 10, 32, 1)
        )

        // when
        val json = testPerson.object2Json()

        // then
        json shouldNotBe null
        println(json)
    }

    @Test
    fun `mapper 로 리스트 형태를 json list 형태로 표시한다`() {

        // given
        val testPersonGroup = listOf<TestPerson>(
            TestPerson("홍길동", 11),
            TestPerson("홍길동", 12, birthday = LocalDate.of(2023, 5, 28)),
            TestPerson("홍길동", 12, birthday2 = LocalDateTime.of(2023, 5, 28, 9, 12, 32)),
        )

        // when
        val jsonList = testPersonGroup.object2Json()

        // then
        jsonList shouldNotBe null
        println(jsonList)
    }

    @Test
    fun `jsonlist 을 클래스로 표시한다`() {

        // given
        val json = """
            [ {
              "name" : "홍길동",
              "age" : 11,
              "birthday" : null,
              "birthday2" : null
            }, {
              "name" : "홍길동",
              "age" : 12,
              "birthday" : "2023-05-28",
              "birthday2" : null
            }, {
              "name" : "홍길동",
              "age" : 12,
              "birthday" : null,
              "birthday2" : "2023-05-28T09:12:32"
            } ]
        """.trimIndent()

        // when
        val testPersons = json.string2Object<List<TestPerson>>()

        // then
        testPersons.size shouldBe 3
    }

    @Test
    fun `json 을 클래스로 표시한다`() {

        // given
        val json = """
            {
              "name" : "홍길동",
              "age" : 12,
              "birthday" : null,
              "birthday2" : "2023-05-28T09:12:32"
            } 
        """.trimIndent()

        // when
        val testPerson = json.string2Object<TestPerson>()

        // then
        testPerson.asClue {
            it.name shouldBe "홍길동"
            it.age shouldBe 12
            it.birthday shouldBe null
            it.birthday2 shouldBe LocalDateTime.of(2023, 5, 28, 9, 12, 32)
        }
    }

    data class TestPerson(
        val name: String,
        val age: Int,
        val birthday: LocalDate? = null,
        val birthday2: LocalDateTime? = null
    )
}
