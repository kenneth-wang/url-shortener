package com.example.urlshortener.urls.controller

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

// In a configuration class:
@Configuration
class TestConfig {
    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .url("jdbc:postgresql://localhost:5435/test_database")
            .username("postgres")
            .password("postgres")
            .build()
    }

    @Bean
    fun testDatabaseSetup(dataSource: DataSource): TestDatabaseSetup {
        return TestDatabaseSetup(dataSource)
    }
}