package com.example.demo.urls.controller

import com.example.demo.urls.configuration.AppConfiguration
import com.example.demo.urls.service.UrlService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

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