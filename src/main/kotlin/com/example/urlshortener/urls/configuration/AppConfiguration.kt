package com.example.urlshortener.urls.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Value("\${baseBackendUrl}")
    lateinit var baseBackendUrl: String

    @Value("\${spring.datasource.url}")
    lateinit var dbUrl: String

    @Value("\${spring.datasource.username}")
    lateinit var dbUsername: String

    @Value("\${spring.datasource.password}")
    lateinit var dbPassword: String

    @Value("\${test.dataFilePath:sql/mock_data_file.sql}")
    lateinit var testDataFilePath: String

    var shortenAlgorithm: String = "RandomAlgorithm"
}