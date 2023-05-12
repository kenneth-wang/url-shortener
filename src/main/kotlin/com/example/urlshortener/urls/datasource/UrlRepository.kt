package com.example.urlshortener.urls.datasource

import com.example.urlshortener.urls.model.Url

interface UrlRepository {

    fun retrieveUrls(): Collection<Url>

    fun retrieveUrl(id: Int): Url?

    fun retrieveByShortUrl(shortUrl: String): Url?

    fun retrieveByOriginalUrl(originalUrl: String): Url?

    fun createUrl(url: Url): Url

    fun deleteUrl(id: Int)

    fun exists(id: Int): Boolean
}