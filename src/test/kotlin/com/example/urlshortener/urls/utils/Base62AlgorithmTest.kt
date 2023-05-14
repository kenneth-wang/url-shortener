package com.example.urlshortener.urls.utils

import com.example.urlshortener.urls.configuration.AppConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Base62AlgorithmTest {
    private val appConfiguration = AppConfiguration().apply {
        baseBackendUrl = "http://localhost:8080"
        shortenAlgorithm = "RandomAlgorithm"
    }
    private val base62Algorithm = Base62Algorithm(appConfiguration)

    @Test
    fun generateShortenedUrl() {
        val num = 1683803574249
        val expectedUrl = "${appConfiguration.baseBackendUrl}/tDWIctj"

        val shortenedUrl = base62Algorithm.generateShortenedUrl(num)
        println("shortenedUrl for Base62AlgorithmTest: $shortenedUrl")

        assertEquals(expectedUrl, shortenedUrl)
    }
}