package com.example.urlshortener.urls.controller

import com.example.urlshortener.urls.datasource.database.DbUrlRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
// Note: Constructor injection requires the @Autowired annotation to indicate that
// Spring should perform dependency injection for the test dependencies.
// This is because the test class itself is not managed by Spring,
// and without the @Autowired annotation, Spring would not be aware that it
// needs to inject the dependencies into the test class.
class UrlControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val dbUrlRepository: DbUrlRepository,
    val testDatabaseSetup: TestDatabaseSetup
) {

    @BeforeEach
    fun setUp() {
        testDatabaseSetup.loadData()
    }

    @Test
    fun getAllUrls() {
        mockMvc.get("/api/urls")
            .andDo { print() }
            .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].id") { value(1) }
            }
    }

    @Test
    @DirtiesContext
    fun createUrl() {
        val originalUrl = "http://localhost:8080"
        val json = """{"originalUrl": "$originalUrl"}"""
        mockMvc.post("/api/urls/") {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(4) }
                jsonPath("$.originalUrl") { value(originalUrl) }
                jsonPath("$.shortUrl") { isNotEmpty() }
                jsonPath("$.createdAt") { isNotEmpty() }
                jsonPath("$.updatedAt") { isNotEmpty() }
            }
    }

    @Test
    fun getUrlById() {
        val id = 1
        mockMvc.get("/api/urls/$id")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(id) }
            }

        val idNotFound = 0
        mockMvc.get("/api/urls/$idNotFound")
            .andDo { print() }
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    @DirtiesContext
    fun deletedUrlById() {
        val id = 1
        mockMvc.delete("/api/urls/$id")
            .andExpect {
                status { isNoContent() }
            }

        val deletedUrl = dbUrlRepository.retrieveUrl(id)
        assertNotNull(deletedUrl?.deletedAt)

        val idNotFound = 5
        mockMvc.delete("/api/urls/$idNotFound")
            .andExpect {
                status { isNotFound() }
            }
    }
}