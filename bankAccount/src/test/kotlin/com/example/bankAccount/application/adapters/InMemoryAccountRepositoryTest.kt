package com.example.bankAccount.application.adapters

import com.example.bankAccount.adapters.InMemoryAccountRepository
import org.junit.jupiter.api.Test
import com.example.bankAccount.domain.Account
import kotlin.test.*

class InMemoryAccountRepositoryTest {

    private val accountRepository = InMemoryAccountRepository()
    private val account = Account(
        id = 1,
        firstName = "Pierre",
        lastName = "Guyard",
        balance = 500.0,
        mapTransactions = linkedMapOf(1 to 50.0, 2 to 20.0, 3 to -15.0)
    )

    @Test
    fun testCreateAccount() {

        accountRepository.save(account)
        val accountCreated = accountRepository.findById(account.id)
        assertEquals(account, accountCreated)

    }

    @Test
    fun testDeleteAccount() {

        // We create an account first
        accountRepository.save(account)
        val accountCreated = accountRepository.findById(1)
        assertEquals(account, accountCreated)

        // Then we delete it
        accountRepository.delete(account.id)
        val accountDeleted = accountRepository.findById(1)
        assertNull(accountDeleted)

    }
}