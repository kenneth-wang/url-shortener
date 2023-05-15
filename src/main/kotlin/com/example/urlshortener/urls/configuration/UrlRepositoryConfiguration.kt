package com.example.urlshortener.urls.configuration

import com.example.urlshortener.urls.datasource.database.DbUrlRepository
import com.example.urlshortener.urls.datasource.database.UrlCrudRepository
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