package com.example.demo.urls.datasource.mock

import org.springframework.stereotype.Repository
import com.example.demo.urls.datasource.UrlRepository
import com.example.demo.urls.model.Url
import org.springframework.context.annotation.Profile

@Profile("test")
class MockUrlRepository : UrlRepository {

    val urls = mutableListOf(
        Url(1, "http://localhost:8080/1", "http://localhost:8080/DNVIHZn"),
        Url(2, "http://localhost:8080/2", "http://localhost:8080/DNWozoH"),
        Url(3, "http://localhost:8080/3", "http://localhost:8080/DNWr6RR"),
    )

    override fun retrieveUrls(): Collection<Url> = urls

    override fun retrieveUrl(id: Int): Url? =
        urls.firstOrNull() { it.id == id }

    override fun retrieveByShortUrl(shortUrl: String): Url? =
        urls.firstOrNull() { it.shortUrl == shortUrl }

    override fun retrieveByOriginalUrl(originalUrl: String): Url? =
        urls.firstOrNull() { it.originalUrl == originalUrl }

    override fun createUrl(url: Url): Url {
        if (urls.any { it.id == url.id }) {
            throw IllegalArgumentException("Url with id ${url.id} already exists.")
        }
        urls.add(url)

        return url
    }

    override fun deleteUrl(id: Int) {
        val currentUrl = urls.firstOrNull { it.id == id }
            ?: throw NoSuchElementException("Could not find an url with id $id")

        urls.remove(currentUrl)
    }

    override fun exists(id: Int): Boolean {
        return urls.any { it.id == id}
    }
}
