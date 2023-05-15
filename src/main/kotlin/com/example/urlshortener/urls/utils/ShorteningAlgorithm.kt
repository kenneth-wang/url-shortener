package com.example.urlshortener.urls.utils

import com.example.urlshortener.urls.configuration.AppConfiguration

interface ShortenAlgorithm {
    fun generateShortenedUrl(num: Long): String
}

class Base62Algorithm(private val appConfiguration: AppConfiguration) : ShortenAlgorithm {
    companion object {
        private const val ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        private const val BASE = ALPHABET.length
    }

    override fun generateShortenedUrl(num: Long): String {
        val sb = StringBuilder()

        var n = num
        while (n > 0) {
            sb.append(ALPHABET[(n % BASE).toInt()])
            n /= BASE
        }

        return "${appConfiguration.baseBackendUrl}/${sb.reverse()}"
    }
}

class RandomAlgorithm(private val appConfiguration: AppConfiguration) : ShortenAlgorithm {
    companion object {
        private const val ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        private const val BASE = ALPHABET.length
    }

    override fun generateShortenedUrl(num: Long): String {
        val sb = StringBuilder()

        var n = num
        while (n > 0) {
            val randomIndex = (Math.random() * BASE).toInt()
            sb.append(ALPHABET[randomIndex])
            n /= BASE
        }

        return "${appConfiguration.baseBackendUrl}/${sb.reverse()}"
    }
}