package com.example.urlshortener.urls.controller

import com.example.urlshortener.urls.model.Url
import com.example.urlshortener.urls.service.UrlService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/urls")
// Does not need @Autowired as the dependencies are passed to the primary constructor.
// In Kotlin, if a class has a primary constructor with dependencies
// declared as primary constructor parameters, Spring can automatically detect and
// inject the dependencies without the need for @Autowired.
class UrlController(private val urlService: UrlService){

    //get all urls
    @GetMapping("")
    fun getAllUrls(): List<Url> =
        urlService.getUrls().toList()

    //create url
    @PostMapping("")
    fun createUrl(@RequestBody url: Url): ResponseEntity<Url> {
        val newUrl = urlService.shortenUrl(url)
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


