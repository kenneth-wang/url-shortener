package com.example.urlshortener.urls.controller

import java.io.File
import javax.sql.DataSource


class TestDatabaseSetup(
    private val dataSource: DataSource
) {
    fun loadData() {
        val file = File("sql/mock_data_file.sql")
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