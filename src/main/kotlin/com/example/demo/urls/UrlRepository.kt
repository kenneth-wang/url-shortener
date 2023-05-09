package com.example.demo.urls

import org.springframework.data.repository.CrudRepository

interface UrlRepository : CrudRepository<Url, Int> {
    fun findByOriginalUrl(originalUrl: String): Url?
    fun findByShortUrl(shortUrl: String): Url?
}