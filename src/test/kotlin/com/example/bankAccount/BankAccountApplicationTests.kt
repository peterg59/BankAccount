package com.example.bankAccount

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest
class BankAccountApplicationTests {

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Test
    fun `Lancement de l'application`() {
        // Vérifie que le contexte Spring se charge correctement
        assertNotNull(applicationContext)
    }
}