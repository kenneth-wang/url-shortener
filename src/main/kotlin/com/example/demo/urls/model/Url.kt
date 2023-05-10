package com.example.demo.urls.model

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
    val shortUrl: String? = null
)