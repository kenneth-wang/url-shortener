package com.example.urlshortener.urls.utils

import com.example.urlshortener.urls.configuration.AppConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
class Base62AlgorithmTest @Autowired constructor(
    private val appConfiguration: AppConfiguration
) {
    private val base62Algorithm = Base62Algorithm(appConfiguration)
    private val num = 1683803574249
    @Test
    fun generateShortenedUrl() {
        val expectedUrl = "${appConfiguration.baseBackendUrl}/DN6SmDt"

        val shortenedUrl = base62Algorithm.generateShortenedUrl(num)

        assertEquals(expectedUrl, shortenedUrl)
    }
}