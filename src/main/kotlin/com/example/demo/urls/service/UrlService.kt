package com.example.demo.urls.service

import com.example.demo.urls.configuration.AppConfiguration
import com.example.demo.urls.datasource.UrlRepository
import com.example.demo.urls.model.Url
import org.springframework.stereotype.Service


@Service
class UrlService (
    private val urlRepository: UrlRepository,
    private val appConfiguration: AppConfiguration
) {

    fun getUrls(): Collection<Url> = urlRepository.retrieveUrls()

    fun getUrl(id: Int): Url? = urlRepository.retrieveUrl(id)

    fun getUrlByShortUrl(shortUrl: String): Url? = urlRepository.retrieveByShortUrl(shortUrl)

    fun addUrl(url: Url): Url = urlRepository.createUrl(url)

    fun deleteUrl(id: Int): Unit = urlRepository.deleteUrl(id)

    fun exists(id: Int): Boolean = urlRepository.exists(id)

    fun shortenUrl(url: Url, num: Long): Url {
        val existingUrl = urlRepository.retrieveByOriginalUrl(url.originalUrl)

        if (existingUrl != null) {
            return existingUrl
        }

        val shortUrl = generateShortUrl(num)
        val newUrl = url.copy(shortUrl=shortUrl)

        return urlRepository.createUrl(newUrl)
    }

    companion object {
        private const val ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        private const val BASE = ALPHABET.length
    }
    fun generateShortUrl(num: Long): String {
        val sb = StringBuilder()

        var n = num
        while (n > 0) {
            sb.append(ALPHABET[(n % BASE).toInt()])
            n /= BASE
        }

        return "${appConfiguration.baseBackendUrl}/${sb.reverse()}"
    }
}