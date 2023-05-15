package com.example.urlshortener.configuration

import com.example.urlshortener.urls.datasource.DbUrlRepository
import com.example.urlshortener.urls.datasource.UrlCrudRepository
import com.example.urlshortener.urls.datasource.UrlRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UrlRepositoryConfiguration {
    @Bean
    fun dbUrlRepository(urlCrudRepository: UrlCrudRepository): UrlRepository {
        return DbUrlRepository(urlCrudRepository)
    }
}