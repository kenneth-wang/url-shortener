package com.example.demo.urls.controller

import com.example.demo.urls.model.Url
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import com.example.demo.urls.datasource.UrlRepository
import com.example.demo.urls.datasource.mock.MockUrlRepository
import org.springframework.test.context.ActiveProfiles

@TestConfiguration
class UrlControllerTestConfiguration {
    @Bean
    fun urlRepository(): UrlRepository {
        return MockUrlRepository()
    }
}

@ActiveProfiles("mock")
@SpringBootTest
@AutoConfigureMockMvc
class UrlControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

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
        val newUrl = Url(id=4, originalUrl="http://localhost:8080")
        mockMvc.post("/api/urls/") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newUrl)
        }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(4) }
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
                jsonPath("$.id") { value(1) }
            }

        val idNotFound = 0
        mockMvc.get("/api/urls/$idNotFound")
            .andDo { print() }
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun deletedUrlById() {
        val id = 1
        mockMvc.delete("/api/urls/$id")
            .andExpect {
                status { isNoContent() }
            }

        val idNotFound = 5
        mockMvc.delete("/api/urls/$idNotFound")
            .andExpect {
                status { isNotFound() }
            }
    }
}