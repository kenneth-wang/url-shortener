package com.example.urlshortener.urls.service

import com.example.urlshortener.urls.configuration.AppConfiguration
import com.example.urlshortener.urls.datasource.UrlRepository
import com.example.urlshortener.urls.model.Url
import com.example.urlshortener.urls.utils.Base62Algorithm
import com.example.urlshortener.urls.utils.RandomAlgorithm
import org.springframework.stereotype.Service
import com.example.urlshortener.urls.utils.ShortenAlgorithm
import java.time.LocalDateTime
import java.time.ZoneOffset

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

    fun shortenUrl(url: Url): Url {
        val algorithm: ShortenAlgorithm = when (appConfiguration.shortenAlgorithm) {
            "Base62Algorithm" -> Base62Algorithm(appConfiguration)
            "RandomAlgorithm" -> RandomAlgorithm(appConfiguration)
            else -> RandomAlgorithm(appConfiguration)
        }
        val num = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
        return generateShortUrl(url, num, algorithm)
    }

    fun generateShortUrl(url: Url, num: Long, algorithm: ShortenAlgorithm): Url {
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
}