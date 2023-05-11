package com.example.demo.urls.datasource.mock

import com.example.demo.urls.model.Url
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.test.annotation.DirtiesContext

internal class MockUrlRepositoryTest {

    private val mockUrlRepository = MockUrlRepository()

    @Test
    fun retrieveUrls() {
        val urls = mockUrlRepository.retrieveUrls()

        assertEquals(3, urls.size)
    }

    @Test
    fun retrieveUrl() {
        val url = mockUrlRepository.retrieveUrl(1)

        assertEquals(
            Url(1, "http://localhost:8080/1", "http://localhost:8080/DNVIHZn"),
            url
        )
    }

    @Test
    fun retrieveByShortUrl() {
        val url = mockUrlRepository.retrieveByShortUrl("http://localhost:8080/DNVIHZn")

        assertEquals(
            Url(1, "http://localhost:8080/1", "http://localhost:8080/DNVIHZn"),
            url
        )
    }

    @Test
    fun retrieveByOriginalUrl() {
        val url = mockUrlRepository.retrieveByOriginalUrl("http://localhost:8080/1")

        assertEquals(Url(1, "http://localhost:8080/1", "http://localhost:8080/DNVIHZn"), url)
    }

    @Test
    @DirtiesContext
    fun createUrl() {
        mockUrlRepository.createUrl(Url(4, "http://localhost:8080/1"))

        val urls = mockUrlRepository.retrieveUrls()
        assertEquals(4, urls.size)
    }

    @Test
    @DirtiesContext
    fun deleteUrl() {
        mockUrlRepository.deleteUrl(1)

        val urls = mockUrlRepository.retrieveUrls()
        assertEquals(2, urls.size)
    }

    @Test
    fun exists() {
        assertTrue(mockUrlRepository.exists(1))
        assertFalse(mockUrlRepository.exists(4))
    }
}