package com.example.urlshortener.urls.utils

import com.example.urlshortener.urls.configuration.AppConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.net.URI

@ActiveProfiles("test")
@SpringBootTest
class RandomAlgorithmTest @Autowired constructor(
    private val appConfiguration: AppConfiguration
){
    private val randomAlgorithm = RandomAlgorithm(appConfiguration)
    private val num = 1683803574249
    @Test
    fun generateShortenedUrl() {
        val shortenedUrl = randomAlgorithm.generateShortenedUrl(num)

        assertEquals(29, shortenedUrl.length)
        assertEquals(appConfiguration.baseBackendUrl, URI(shortenedUrl).scheme + "://" + URI(shortenedUrl).authority)
    }
}