package com.example.bankAccount.application.usecases

import com.example.bankAccount.application.ports.out.AccountRepository
import com.example.bankAccount.model.Account
import io.mockk.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class ViewBalanceUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val viewBalanceUseCase = ViewBalanceUseCase(accountRepository)

    @Test
    fun testViewBalance() {

        val account = Account(
            id = 1,
            firstName = "Pierre",
            lastName = "Guyard",
            balance = 500.0,
            mapTransactions = linkedMapOf(1 to 50.0, 2 to 20.0, 3 to -15.0)
        )

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        var balance = viewBalanceUseCase.getBalance(1)

        assertEquals(500.0, balance)
    }

    @Test
    fun testIfAccountNotFoundThrowException() {

        every { accountRepository.findById(1) } returns null

        assertFailsWith<IllegalArgumentException> {
            viewBalanceUseCase.getBalance(1)
        }
    }
}
