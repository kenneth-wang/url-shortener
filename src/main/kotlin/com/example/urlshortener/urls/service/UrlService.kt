package com.example.urlshortener.urls.service

import com.example.urlshortener.urls.configuration.AppConfiguration
import com.example.urlshortener.urls.datasource.UrlRepository
import com.example.urlshortener.urls.model.Url
import org.springframework.stereotype.Service
import com.example.urlshortener.urls.utils.ShortenAlgorithm

@Service
class UrlService (
    private val urlRepository: UrlRepository,
    private val appConfiguration: AppConfiguration
) {

    companion object {
        private const val ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        private const val BASE = ALPHABET.length
    }

    fun getUrls(): Collection<Url> = urlRepository.retrieveUrls()

    fun getUrl(id: Int): Url? = urlRepository.retrieveUrl(id)

    fun getUrlByShortUrl(shortUrl: String): Url? = urlRepository.retrieveByShortUrl(shortUrl)

    fun addUrl(url: Url): Url = urlRepository.createUrl(url)

    fun deleteUrl(id: Int): Unit = urlRepository.deleteUrl(id)

    fun exists(id: Int): Boolean = urlRepository.exists(id)

    fun shortenUrl(url: Url, num: Long, algorithm: ShortenAlgorithm): Url {
        var originalUrl = url.originalUrl.trim()

        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            // Assume that all websites have http to https redirection
            originalUrl = "http://$originalUrl"
        }

        val existingUrl = urlRepository.retrieveByOriginalUrl(originalUrl)
        if (existingUrl != null) {
            return existingUrl
        }

        val shortUrl = algorithm.generateShortenedUrl(num)
        val newUrl = url.copy(originalUrl=originalUrl, shortUrl=shortUrl)

        return urlRepository.createUrl(newUrl)
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