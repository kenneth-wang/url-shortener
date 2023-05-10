package com.example.demo.urls.service

import com.example.demo.urls.datasource.UrlRepository
import com.example.demo.urls.model.Url
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class UrlService(private val urlRepository: UrlRepository) {

    fun shortenUrl(originalUrl: String): String? {
        val existingUrl = urlRepository.findByOriginalUrl(originalUrl)

        if (existingUrl != null) {
            return existingUrl.shortUrl
        }

        val shortUrl = generateShortUrl()
        val url = Url(id=null, shortUrl=shortUrl, originalUrl=originalUrl)
        urlRepository.save(url)

        return shortUrl
    }

    private fun generateShortUrl(): String {
        val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val base = alphabet.length

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