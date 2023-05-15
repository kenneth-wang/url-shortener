package com.example.urlshortener.urls.controller

import com.example.urlshortener.urls.configuration.AppConfiguration
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration

class TestConfig {

    // Note: Do not need to add @Bean here.
    // By having the @Configuration annotation on the AppConfiguration class,
    // Spring will treat it as a configuration class and use the existing instance of AppConfiguration as a bean.
    fun appConfiguration(): AppConfiguration {
        return AppConfiguration()
    }

    @Bean
    fun dataSource(appConfiguration: AppConfiguration): DataSource {
        return DataSourceBuilder.create()
            .url(appConfiguration.dbUrl)
            .username(appConfiguration.dbUsername)
            .password(appConfiguration.dbPassword)
            .build()
    }

    @Bean
    fun testDatabaseSetup(dataSource: DataSource, appConfiguration: AppConfiguration): TestDatabaseSetup {
        return TestDatabaseSetup(dataSource, appConfiguration)
    }
}