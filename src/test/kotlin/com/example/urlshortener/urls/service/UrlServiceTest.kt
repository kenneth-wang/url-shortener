package com.example.urlshortener.urls.service

import com.example.urlshortener.urls.configuration.AppConfiguration
import com.example.urlshortener.urls.controller.TestDatabaseSetup
import com.example.urlshortener.urls.datasource.database.DbUrlRepository
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
    val testDatabaseSetup: TestDatabaseSetup,
    val appConfiguration: AppConfiguration
){

    @BeforeEach
    fun setUp() {
        testDatabaseSetup.loadData()
    }

    private val urlRepository: UrlRepository = mockk(relaxed = true)
    private val urlService = UrlService(urlRepository, appConfiguration)
    private val num = 1683803574249
    private val originalUrl = "example.com"
    private val baseBackendUrl = appConfiguration.baseBackendUrl
    private val shortUrl = "$baseBackendUrl/<shortened_url>"
    private val newId = 4
    private val existingId = 1

    @Test
    fun getUrls() {
        urlService.getUrls()
        verify(exactly=1) { urlRepository.retrieveUrls() }
    }

    @Test
    fun getUrl() {
        urlService.getUrl(existingId)
        verify(exactly=1) { urlRepository.retrieveUrl(existingId) }
    }

    @Test
    fun getUrlByShortUrl() {
        urlService.getUrlByShortUrl(shortUrl)
        verify(exactly=1) { urlRepository.retrieveByShortUrl(shortUrl) }
    }

    @Test
    fun addUrl() {
        urlService.addUrl(Url(newId, "$baseBackendUrl/$newId", shortUrl))
        verify(exactly=1) {
            urlRepository.createUrl(
                any()
            )
        }
    }

    @Test
    fun deleteUrl() {
        urlService.deleteUrl(existingId)
        verify(exactly=1) { urlRepository.deleteUrl(existingId) }
    }

    @Test
    fun exists() {
        urlService.exists(existingId)
        verify(exactly=1) { urlRepository.exists(existingId) }
    }

    @Test
    fun shortenUrl() {
        val shortenAlgorithm = mockk<ShortenAlgorithm>()

        every { shortenAlgorithm.generateShortenedUrl(any()) } returns shortUrl

        val modifiedOriginalUrl = "http://$originalUrl"
        val urlService = UrlService(dbUrlRepository, appConfiguration)
        val url = urlService.shortenUrl(Url(newId, originalUrl, null))

        assertEquals(modifiedOriginalUrl, url.originalUrl)
        assertNotNull(url.shortUrl)
    }

    @Nested
    inner class GenerateShortUrlTests {
        @Test
        fun `generateShortUrl should create new Url object and return it when given url without http or https prefix`() {
            val shortenAlgorithm = mockk<ShortenAlgorithm>()

            every { shortenAlgorithm.generateShortenedUrl(num) } returns shortUrl

            val urlService = UrlService(dbUrlRepository, appConfiguration)
            val url = urlService.generateShortUrl(Url(newId, originalUrl, null), num, shortenAlgorithm)

            assertEquals("http://$originalUrl", url.originalUrl)
            assertEquals(shortUrl, url.shortUrl)
        }

        @Test
        fun `generateShortUrl should create new Url object and return it when given url with extra spaces`() {
            val shortenAlgorithm = mockk<ShortenAlgorithm>()

            every { shortenAlgorithm.generateShortenedUrl(num) } returns shortUrl

            val originalUrlWithHttp = "   http://$originalUrl   "
            val urlService = UrlService(dbUrlRepository, appConfiguration)
            val url = urlService.generateShortUrl(Url(newId, originalUrlWithHttp, null), num, shortenAlgorithm)

            assertEquals("http://$originalUrl", url.originalUrl)
        }

        @Test
        @DirtiesContext
        fun generateShortUrl() {
            val shortenAlgorithm = mockk<ShortenAlgorithm>()

            every { urlRepository.retrieveByOriginalUrl("http://$originalUrl") } returns null
            every { shortenAlgorithm.generateShortenedUrl(num) } returns shortUrl

            val urlService = UrlService(urlRepository, appConfiguration)

            urlService.generateShortUrl(Url(newId, originalUrl, null), num, shortenAlgorithm)

            verify(exactly = 1) { urlRepository.retrieveByOriginalUrl("http://$originalUrl") }
            verify(exactly = 1) { shortenAlgorithm.generateShortenedUrl(num) }
        }
    }
}