package com.example.bankAccount.adapters.repository

import org.junit.jupiter.api.Test
import com.example.bankAccount.domain.model.Account
import java.math.BigDecimal
import kotlin.test.*

class AccountRepositoryTest {

    private val inMemoryAccountRepository = InMemoryAccountRepository()
    private val account = Account(
        id = 1,
        firstName = "Pierre",
        lastName = "Guyard",
        balance = BigDecimal(500),
        transactions = mutableListOf(BigDecimal(50), BigDecimal(20), BigDecimal(-15))
    )

    @Test
    fun testCreateAccount() {

        inMemoryAccountRepository.save(account)
        val accountCreated = inMemoryAccountRepository.findById(account.id)
        assertEquals(account, accountCreated)

    }

    @Test
    fun testDeleteAccount() {

        // We create an account first
        inMemoryAccountRepository.save(account)
        val accountCreated = inMemoryAccountRepository.findById(1)
        assertEquals(account, accountCreated)

        // Then we delete it
        inMemoryAccountRepository.delete(account.id)
        val accountDeleted = inMemoryAccountRepository.findById(1)
        assertNull(accountDeleted)

    }
}