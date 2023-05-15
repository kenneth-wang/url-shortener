package com.example.urlshortener.urls.datasource

import com.example.urlshortener.urls.model.Url
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

interface UrlCrudRepository : CrudRepository<Url, Int> {
    fun findByOriginalUrl(originalUrl: String): Url?
    fun findByShortUrl(shortUrl: String): Url?
}

@Profile("database")
class DbUrlRepository(@Autowired private val urlCrudRepository: UrlCrudRepository) : UrlRepository {

    override fun retrieveUrls(): Collection<Url> = urlCrudRepository.findAll().toList()

    override fun retrieveUrl(id: Int): Url? = urlCrudRepository.findById(id).orElse(null)

    override fun retrieveByShortUrl(shortUrl: String): Url? = urlCrudRepository.findByShortUrl(shortUrl)

    override fun retrieveByOriginalUrl(originalUrl: String): Url? = urlCrudRepository.findByOriginalUrl(originalUrl)

    override fun createUrl(url: Url): Url = urlCrudRepository.save(url)

    override fun deleteUrl(id: Int) {
        val url = urlCrudRepository.findById(id).orElse(null)
        url?.let {
            it.deletedAt = LocalDateTime.now()
            urlCrudRepository.save(it)
        }
    }

    override fun exists(id: Int) = urlCrudRepository.existsById(id)

    fun deleteAll() = urlCrudRepository.deleteAll()
}