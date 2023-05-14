package com.example.urlshortener.urls.utils

import com.example.urlshortener.urls.configuration.AppConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

class RandomAlgorithmTest {
    private val appConfiguration = AppConfiguration().apply {
        baseBackendUrl = "http://localhost:8080"
        shortenAlgorithm = "RandomAlgorithm"
    }
    private val randomAlgorithm = RandomAlgorithm(appConfiguration)

    @Test
    fun generateShortenedUrl() {
        val num = 1683803574249

        val shortenedUrl = randomAlgorithm.generateShortenedUrl(num)

        assertEquals(29, shortenedUrl.length)
        assertEquals(appConfiguration.baseBackendUrl, URI(shortenedUrl).scheme + "://" + URI(shortenedUrl).authority)
    }
}