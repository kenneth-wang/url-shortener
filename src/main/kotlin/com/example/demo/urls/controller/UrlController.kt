package com.example.demo.urls.controller

import com.example.demo.urls.configuration.AppConfiguration
import com.example.demo.urls.model.Url
import com.example.demo.urls.service.UrlService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import java.time.LocalDateTime
import java.time.ZoneOffset

@RestController
@RequestMapping("")
class ShortenedUrlController(
    private val service: UrlService,
    private val appConfiguration: AppConfiguration
) {
    //get url by shortUrl
    @GetMapping("/{shortUrl}")
    fun redirect(@PathVariable shortUrl: String, response: HttpServletResponse): ResponseEntity<String> {
        val url = service.getUrlByShortUrl("${appConfiguration.baseBackendUrl}/$shortUrl")
        return if (url != null) {
            response.sendRedirect(url.originalUrl)
            return ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}

@RestController
@RequestMapping("/api/urls")
class UrlController(@Autowired private val urlService: UrlService){

    //get all urls
    @GetMapping("")
    fun getAllUrls(): List<Url> =
        urlService.getUrls().toList()

    //create url
    @PostMapping("")
    fun createUrl(@RequestBody url: Url): ResponseEntity<Url> {
        val newUrl = urlService.shortenUrl(url, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
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


