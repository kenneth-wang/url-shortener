package com.example.urlshortener.urls.datasource

import com.example.urlshortener.urls.model.Url
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.data.repository.CrudRepository

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

    override fun deleteUrl(id: Int) = urlCrudRepository.deleteById(id)

    override fun exists(id: Int) = urlCrudRepository.existsById(id)
}