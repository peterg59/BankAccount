package com.example.bankAccount.adapters.repository

import org.junit.jupiter.api.Test
import com.example.bankAccount.domain.Account
import org.iban4j.Iban
import java.math.BigDecimal
import kotlin.test.*

class AccountRepositoryTest {

    private val inMemoryAccountRepository = InMemoryAccountRepository()
    private val account = Account(
        iban = Iban.random().toString(),
        firstName = "John",
        lastName = "Doe",
        balance = BigDecimal.ZERO,
        transactions = emptyList()
    )

    @Test
    fun `Ouverture d'un compte bancaire`() {

        val accountOpened = inMemoryAccountRepository.openAccount(account)
        assertEquals(account, accountOpened)
    }

    @Test
    fun `Consultation d'un compte bancaire`() {

        // Ouverture d'un compte
        val accountOpened = inMemoryAccountRepository.openAccount(account)
        val accountConsulted = inMemoryAccountRepository.consultAccount(accountOpened.iban)

        assertEquals(account, accountConsulted)
    }

    @Test
    fun `Fermeture d'un compte bancaire`() {

        // Ouverture d'un compte
        val accountOpened = inMemoryAccountRepository.openAccount(account)
        assertEquals(account, accountOpened)

        // Ensuite fermeture de celui-ci
        inMemoryAccountRepository.closeAccount(accountOpened.iban)
        val accountClosed = inMemoryAccountRepository.consultAccount(accountOpened.iban)
        assertNull(accountClosed)
    }
}
