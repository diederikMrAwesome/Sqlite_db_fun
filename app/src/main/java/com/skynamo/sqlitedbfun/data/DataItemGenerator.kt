package com.skynamo.sqlitedbfun.data

import kotlin.random.Random

class DataItemGenerator {
    fun generateRandomUser(): DataItem {
        val id = Random.nextInt(1, 1000)
        val username = generateRandomString(8)
        val email = generateRandomEmail(username)
        val age = Random.nextInt(8, 80)
        val isActive = Random.nextBoolean()

        return DataItem(id, username, email, age, isActive)
    }

    private fun generateRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun generateRandomEmail(username: String): String {
        val domains = listOf("example.com", "test.com", "domain.com")
        val domain = domains.random()
        return "$username@$domain"
    }
}
