package com.github.ryebook

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Component(value = "MysqlTestContainer")
@Testcontainers
class MysqlTestContainer {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private val mysqlImageName = if (System.getProperty("os.arch") == "aarch64") {
            DockerImageName.parse("arm64v8/mysql:8.0").asCompatibleSubstituteFor("mysql")
        } else {
            DockerImageName.parse("mysql:8.0.26")
        }

        @JvmStatic
        @Container
        val MYSQL_CONTAINER = SpecifiedMySQLContainer(image = mysqlImageName)
            .apply {
                withDatabaseName("testdb")
                withUsername("testroot")
                withPassword("testrootpass")
                withCommand("--character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci")
                withUrlParam("useTimezone", "true")
                withUrlParam("serverTimezone", "Asia/Seoul")
                start()
            }
    }

    @PostConstruct
    fun init() {
        log.info("@@@@@ [started] mysql container @@@@@")
        log.info("@@@@@ imageName : ${MYSQL_CONTAINER.dockerImageName}")
        log.info("@@@@@ databaseName : ${MYSQL_CONTAINER.databaseName}")
        log.info("@@@@@ username : ${MYSQL_CONTAINER.username}")
        log.info("@@@@@ password : ${MYSQL_CONTAINER.password}")
        log.info("@@@@@ jdbcUrl : ${MYSQL_CONTAINER.jdbcUrl}")
        log.info("@@@@@ exposedPorts : ${MYSQL_CONTAINER.exposedPorts}")
    }

    @PreDestroy
    fun beforeDestroy() {
        MYSQL_CONTAINER.stop()
        log.info("@@@@@ [stopped] mysql container @@@@@")
    }

    // https://github.com/testcontainers/testcontainers-java/issues/318
    class SpecifiedMySQLContainer(image: DockerImageName) : MySQLContainer<SpecifiedMySQLContainer>(image)
}
