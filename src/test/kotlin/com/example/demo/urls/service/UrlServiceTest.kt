package com.example.demo.urls.service

import com.example.demo.urls.datasource.UrlRepository
import com.example.demo.urls.model.Url
import org.junit.jupiter.api.Test

import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.annotation.DirtiesContext

class UrlServiceTest {

    private val urlRepository: UrlRepository = mockk(relaxed = true)
    private val urlService = UrlService(urlRepository)

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
        verify(exactly=1) { urlRepository.createUrl(Url(4, "http://localhost:8080/4", "http://localhost:8080/<shortened_url>")) }
    }

    @Test
    fun deleteUrl() {
        urlService.deleteUrl(1)
        verify(exactly=1) { urlRepository.deleteUrl(1) }
    }

    @Test
    @DirtiesContext
    fun shortenUrl() {
        urlService.shortenUrl("http://localhost:8080")
        verify(exactly=1) { urlRepository.retrieveByOriginalUrl("http://localhost:8080")}
    }

    @Test
    fun exists() {
        urlService.exists(1)
        verify(exactly=1) { urlRepository.exists(1) }
    }
}