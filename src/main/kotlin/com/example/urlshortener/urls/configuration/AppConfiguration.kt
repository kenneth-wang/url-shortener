package com.example.urlshortener.urls.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
class AppConfiguration {

    @Value("\${baseBackendUrl}")
    lateinit var baseBackendUrl: String
}