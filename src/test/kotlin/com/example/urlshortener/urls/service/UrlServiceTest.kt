package com.example.urlshortener.urls.service

import com.example.urlshortener.urls.configuration.AppConfiguration
import com.example.urlshortener.urls.datasource.UrlRepository
import com.example.urlshortener.urls.model.Url
import com.example.urlshortener.urls.utils.ShortenAlgorithm
import io.mockk.every
import org.junit.jupiter.api.Test

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.test.annotation.DirtiesContext

class UrlServiceTest {

    private val urlRepository: UrlRepository = mockk(relaxed = true)
    private val appConfiguration = AppConfiguration().apply {
        baseBackendUrl = "http://localhost:8080"
        shortenAlgorithm = "RandomAlgorithm"
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
        val num = 1683803574249
        val shortenAlgorithm = mockk<ShortenAlgorithm>()

        every { urlRepository.retrieveByOriginalUrl("http://localhost:8080") } returns null
        every { shortenAlgorithm.generateShortenedUrl(num) } returns "http://localhost:8080/shortened-url"

        // create a new instance of UrlService that uses the mock ShortenAlgorithm
        val urlService = UrlService(urlRepository, appConfiguration)

        urlService.shortenUrl(Url(4, "http://localhost:8080", null), num, shortenAlgorithm)

        verify(exactly=1) { urlRepository.retrieveByOriginalUrl("http://localhost:8080")}
        verify(exactly=1) { shortenAlgorithm.generateShortenedUrl(num) }
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