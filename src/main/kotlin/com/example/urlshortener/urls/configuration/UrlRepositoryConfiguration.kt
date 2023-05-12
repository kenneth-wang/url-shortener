package com.example.urlshortener.configuration

import com.example.urlshortener.urls.datasource.DbUrlRepository
import com.example.urlshortener.urls.datasource.UrlCrudRepository
import com.example.urlshortener.urls.datasource.UrlRepository
import com.example.urlshortener.urls.datasource.mock.MockUrlRepository
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