package com.example.urlshortener.urls.controller

import com.example.urlshortener.urls.configuration.AppConfiguration
import com.example.urlshortener.urls.model.Url
import com.example.urlshortener.urls.service.UrlService
import com.example.urlshortener.urls.utils.Base62Algorithm
import com.example.urlshortener.urls.utils.RandomAlgorithm
import com.example.urlshortener.urls.utils.ShortenAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.ZoneOffset

@RestController
@RequestMapping("/api/urls")
class UrlController(@Autowired private val urlService: UrlService, private val appConfiguration: AppConfiguration){

    //get all urls
    @GetMapping("")
    fun getAllUrls(): List<Url> =
        urlService.getUrls().toList()

    //create url
    @PostMapping("")
    fun createUrl(@RequestBody url: Url): ResponseEntity<Url> {
        val algorithm: ShortenAlgorithm = when (appConfiguration.shortenAlgorithm) {
            "Base62Algorithm" -> Base62Algorithm(appConfiguration)
            "RandomAlgorithm" -> RandomAlgorithm(appConfiguration)
            else -> RandomAlgorithm(appConfiguration)
        }
        val newUrl = urlService.shortenUrl(url, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(), algorithm)
        return ResponseEntity(newUrl, HttpStatus.CREATED)
    }

    //get url by id
    @GetMapping("/{id}")
    fun getUrlById(@PathVariable("id") urlId: Int): ResponseEntity<Url> {
        val url = urlService.getUrl(urlId) ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        return ResponseEntity(url, HttpStatus.OK)
    }

    //delete url
    @DeleteMapping("/{id}")
    fun deletedUrlById(@PathVariable("id") urlId: Int): ResponseEntity<Url> {
        if (!urlService.exists(urlId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        urlService.deleteUrl(urlId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}


