package com.example.urlshortener.urls.model

import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "urls")
data class Url(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,
    @Column(name = "original_url", nullable = false)
    val originalUrl: String,

    @Column(name = "short_url", unique = true)
    val shortUrl: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
)
