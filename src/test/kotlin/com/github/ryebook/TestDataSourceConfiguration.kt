package com.github.ryebook

import com.github.ryebook.MysqlTestContainerConfiguration.Companion.MYSQL_CONTAINER
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.Database
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.statemachine.config.EnableStateMachine
import org.springframework.transaction.PlatformTransactionManager
import java.util.Properties
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@TestConfiguration
@EnableStateMachine
class TestDataSourceConfiguration {

    @Bean
    @DependsOn("MysqlTestContainer")
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .url(MYSQL_CONTAINER.jdbcUrl)
            .driverClassName(MYSQL_CONTAINER.driverClassName)
            .username(MYSQL_CONTAINER.username)
            .password(MYSQL_CONTAINER.password)
            .build()
    }

    @Bean
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {

        val vendorAdapter = HibernateJpaVendorAdapter().apply {
            this.setGenerateDdl(true)
            this.setShowSql(true)
            this.setDatabase(Database.MYSQL)
        }

        val properties = Properties().apply {
            this.setProperty("hibernate.show_sql", "true")
            this.setProperty("hibernate.format_sql", "true")
            this.setProperty("hibernate.hbm2ddl.auto", "create-drop")
            this.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
        }

        return LocalContainerEntityManagerFactoryBean().apply {
            this.jpaVendorAdapter = vendorAdapter
            this.dataSource = dataSource()
            this.setPackagesToScan("com.github.ryebook", "org.springframework.statemachine.data")
            this.setJpaProperties(properties)
        }
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager().apply {
            this.entityManagerFactory = entityManagerFactory
        }
    }
}
