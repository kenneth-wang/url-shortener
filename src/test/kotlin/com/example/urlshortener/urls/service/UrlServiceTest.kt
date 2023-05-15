package com.example.urlshortener.urls.service

import com.example.urlshortener.urls.configuration.AppConfiguration
import com.example.urlshortener.urls.controller.TestDatabaseSetup
import com.example.urlshortener.urls.datasource.DbUrlRepository
import com.example.urlshortener.urls.datasource.UrlRepository
import com.example.urlshortener.urls.model.Url
import com.example.urlshortener.urls.utils.ShortenAlgorithm
import io.mockk.every
import org.junit.jupiter.api.Test

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
@SpringBootTest
class UrlServiceTest @Autowired constructor(
    val dbUrlRepository: DbUrlRepository,
    val testDatabaseSetup: TestDatabaseSetup
){

    @BeforeEach
    fun setUp() {
        testDatabaseSetup.loadData()
    }

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
                any()
            )
        }
    }

    @Test
    fun deleteUrl() {
        urlService.deleteUrl(1)
        verify(exactly=1) { urlRepository.deleteUrl(1) }
    }

    @Test
    fun exists() {
        urlService.exists(1)
        verify(exactly=1) { urlRepository.exists(1) }
    }

    @Test
    fun shortenUrl() {
        val shortenAlgorithm = mockk<ShortenAlgorithm>()

        every { shortenAlgorithm.generateShortenedUrl(any()) } returns "http://localhost:8080/shortened-url"

        val originalUrl = "example.com"
        val urlService = UrlService(dbUrlRepository, appConfiguration)
        val url = urlService.shortenUrl(Url(4, originalUrl, null))

        assertEquals("http://example.com", url.originalUrl)
        assertNotNull(url.shortUrl)
    }

    @Nested
    inner class GenerateShortUrlTests {
        @Test
        fun `generateShortUrl should create new Url object and return it when given url without http or https prefix`() {
            val num = 1683803574249
            val shortenAlgorithm = mockk<ShortenAlgorithm>()

            every { shortenAlgorithm.generateShortenedUrl(num) } returns "http://localhost:8080/shortened-url"

            val originalUrl = "example.com"
            val urlService = UrlService(dbUrlRepository, appConfiguration)
            val url = urlService.generateShortUrl(Url(4, originalUrl, null), num, shortenAlgorithm)

            assertEquals("http://example.com", url.originalUrl)
            assertEquals("http://localhost:8080/shortened-url", url.shortUrl)
        }

        @Test
        fun `generateShortUrl should create new Url object and return it when given url with extra spaces`() {
            val num = 1683803574249
            val shortenAlgorithm = mockk<ShortenAlgorithm>()

            every { shortenAlgorithm.generateShortenedUrl(num) } returns "http://localhost:8080/shortened-url"

            val originalUrl = "   http://www.example.com   "
            val urlService = UrlService(dbUrlRepository, appConfiguration)
            val url = urlService.generateShortUrl(Url(4, originalUrl, null), num, shortenAlgorithm)

            assertEquals("http://www.example.com", url.originalUrl)
        }

        @Test
        @DirtiesContext
        fun generateShortUrl() {
            val num = 1683803574249
            val shortenAlgorithm = mockk<ShortenAlgorithm>()

            every { urlRepository.retrieveByOriginalUrl("http://localhost:8080") } returns null
            every { shortenAlgorithm.generateShortenedUrl(num) } returns "http://localhost:8080/shortened-url"

            val urlService = UrlService(urlRepository, appConfiguration)

            urlService.generateShortUrl(Url(4, "http://localhost:8080", null), num, shortenAlgorithm)

            verify(exactly = 1) { urlRepository.retrieveByOriginalUrl("http://localhost:8080") }
            verify(exactly = 1) { shortenAlgorithm.generateShortenedUrl(num) }
        }
    }
}