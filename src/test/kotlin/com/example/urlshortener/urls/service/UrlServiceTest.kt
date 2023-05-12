package com.example.urlshortener.urls.service

import com.example.urlshortener.urls.configuration.AppConfiguration
import com.example.urlshortener.urls.datasource.UrlRepository
import com.example.urlshortener.urls.model.Url
import org.junit.jupiter.api.Test

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.test.annotation.DirtiesContext

class UrlServiceTest {

    private val urlRepository: UrlRepository = mockk(relaxed = true)
    private val appConfiguration = AppConfiguration().apply {
        baseBackendUrl = "http://localhost:8080"
    }
    private val urlService = UrlService(urlRepository, appConfiguration)

    @Test
    fun getUrls() {
        urlService.getUrls()
        verify(exactly=1) { urlRepository.retrieveUrls() }
    }

    @Test
    fun getUrl() {
        urlService.getUrl(1)
        verify(exactly=1) { urlRepository.retrieveUrl(1) }
    }

    @Test
    fun getUrlByShortUrl() {
        urlService.getUrlByShortUrl("http://localhost:8080/<shortened_url>")
        verify(exactly=1) { urlRepository.retrieveByShortUrl("http://localhost:8080/<shortened_url>") }
    }

    @Test
    fun addUrl() {
        urlService.addUrl(Url(4, "http://localhost:8080/4", "http://localhost:8080/<shortened_url>"))
        verify(exactly=1) {
            urlRepository.createUrl(
                Url(4, "http://localhost:8080/4", "http://localhost:8080/<shortened_url>")
            )
        }
    }

    @Test
    fun deleteUrl() {
        urlService.deleteUrl(1)
        verify(exactly=1) { urlRepository.deleteUrl(1) }
    }

    @Test
    @DirtiesContext
    fun shortenUrl() {
        urlService.shortenUrl(Url(4, "http://localhost:8080", null), 1683803574249)
        verify(exactly=1) { urlRepository.retrieveByOriginalUrl("http://localhost:8080")}
    }

    @Test
    fun exists() {
        urlService.exists(1)
        verify(exactly=1) { urlRepository.exists(1) }
    }

    @Test
    fun generateShortUrl() {
        val num = 1683803574249
        val expectedShortUrl = "http://localhost:8080/DN6SmDt"
        val actualShortUrl = urlService.generateShortUrl(num)
        assertEquals(expectedShortUrl, actualShortUrl)
    }
}