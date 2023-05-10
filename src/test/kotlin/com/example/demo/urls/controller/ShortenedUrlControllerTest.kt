package com.example.demo.urls.controller

import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@ActiveProfiles("mock")
@SpringBootTest
@AutoConfigureMockMvc
class ShortenedUrlControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
) {

    @Test
    fun redirect() {
        val shortUrlSuffix = "DNVIHZn"
        mockMvc.get("/$shortUrlSuffix")
            .andExpect {
                status { is3xxRedirection() }
                redirectedUrl("http://localhost:8080/1")
            }
    }
}