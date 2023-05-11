package com.example.demo.configuration

import com.example.demo.urls.datasource.DbUrlRepository
import com.example.demo.urls.datasource.UrlCrudRepository
import com.example.demo.urls.datasource.UrlRepository
import com.example.demo.urls.datasource.mock.MockUrlRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class UrlRepositoryConfiguration {
    @Bean
    @Profile("database")
    fun dbUrlRepository(urlCrudRepository: UrlCrudRepository): UrlRepository {
        return DbUrlRepository(urlCrudRepository)
    }

    @Bean
    @Profile("mock")
    fun mockUrlRepository(): UrlRepository {
        return MockUrlRepository()
    }
}