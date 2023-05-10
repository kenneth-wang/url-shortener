package com.example.demo.urls.service

import com.example.demo.urls.datasource.UrlRepository
import com.example.demo.urls.model.Url
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class UrlService (
    private val urlRepository: UrlRepository
) {

    fun getUrls(): Collection<Url> = urlRepository.retrieveUrls()

    fun getUrl(id: Int): Url? = urlRepository.retrieveUrl(id)

    fun getUrlByShortUrl(shortUrl: String): Url? = urlRepository.retrieveByShortUrl(shortUrl)

    fun addUrl(url: Url): Url = urlRepository.createUrl(url)

    fun deleteUrl(id: Int): Unit = urlRepository.deleteUrl(id)

    fun exists(id: Int): Boolean = urlRepository.exists(id)

    fun  shortenUrl(originalUrl: String): String? {
        val existingUrl = urlRepository.retrieveByOriginalUrl(originalUrl)

        if (existingUrl != null) {
            return existingUrl.shortUrl
        }

        val shortUrl = generateShortUrl()
        val url = Url(id=null, shortUrl=shortUrl, originalUrl=originalUrl)
        urlRepository.createUrl(url)

        return shortUrl
    }

    private fun generateShortUrl(): String {
        val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val base = alphabet.length
        // Use Companion object to convert variables to constants
        // https://docs.oracle.com/javase/8/docs/api/java/security/SecureRandom.html
        var num: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
        val sb = StringBuilder()

        while (num > 0) {
            sb.append(alphabet[(num % base).toInt()])
            num /= base
        }

        val baseUrl = "http://localhost:8080"
        val subUrl = sb.reverse().toString()
        return "$baseUrl/$subUrl"
    }

}