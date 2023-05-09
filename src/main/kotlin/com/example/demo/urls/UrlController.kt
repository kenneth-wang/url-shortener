package com.example.demo.urls

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource


@RestController
@RequestMapping("")
class ShortenedUrlController(
    @Autowired private val urlRepository: UrlRepository) {

    @Autowired
    lateinit var appConfiguration: AppConfiguration

    //get url by shortUrl
    @GetMapping("/{shortUrl}")
    fun redirect(@PathVariable shortUrl: String, response: HttpServletResponse): ResponseEntity<Unit> {
        val url = urlRepository.findByShortUrl("${appConfiguration.baseBackendUrl}/$shortUrl")
        if (url != null) {
            response.sendRedirect(url.originalUrl)
            return ResponseEntity(HttpStatus.OK)
        }

        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}


@RestController
@RequestMapping("/api/urls")
class UrlController(@Autowired private val urlRepository: UrlRepository, val urlService: UrlService){

    //get all urls
    @GetMapping("")
    fun getAllUrls(): List<Url> =
        urlRepository.findAll().toList()

    //create url
    @PostMapping("")
    fun createUrl(@RequestBody url: Url): ResponseEntity<Url> {
        val shortUrl = urlService.shortenUrl(url.originalUrl)
        val newUrl = url.copy(shortUrl=shortUrl)
        return ResponseEntity(newUrl, HttpStatus.CREATED)
    }

    //get url by id
    @GetMapping("/{id}")
    fun getUrlById(@PathVariable("id") urlId: Int): ResponseEntity<Url> {
        val url = urlRepository.findById(urlId).orElse(null)
        return if (url != null) {
            ResponseEntity(url, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //delete url
    @DeleteMapping("/{id}")
    fun deletedUrlById(@PathVariable("id") urlId: Int): ResponseEntity<Url> {
        if (!urlRepository.existsById(urlId)){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        urlRepository.deleteById(urlId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}


@Configuration
@PropertySource("classpath:application.yml")
class AppConfiguration {

    @Value("\${baseBackendUrl}")
    lateinit var baseBackendUrl: String
}