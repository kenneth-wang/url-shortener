package com.example.urlshortener.urls.controller

import com.example.urlshortener.urls.configuration.AppConfiguration
import java.io.File
import javax.sql.DataSource


class TestDatabaseSetup(
    private val dataSource: DataSource,
    private val appConfiguration: AppConfiguration
) {
    fun loadData() {
        val file = File(appConfiguration.testDataFilePath)
        val inputStream = file.inputStream()
        val reader = inputStream.bufferedReader()
        val script = reader.use { it.readText() }
        val statements = script.split(";")

        val connection = dataSource.connection

        try {
            for (statement in statements) {
                if (statement.isNotBlank()) {
                    connection.prepareStatement(statement).execute()
                }
            }
        } finally {
            connection.close()
        }
    }
}