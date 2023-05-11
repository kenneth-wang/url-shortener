package com.example.demo.urls.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.yml")
class AppConfiguration {

    @Value("\${spring.baseBackendUrl}")
    lateinit var baseBackendUrl: String
}