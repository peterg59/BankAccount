package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.ports.out.AccountRepository
import com.example.bankAccount.domain.model.Account
import io.mockk.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
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
            balance = BigDecimal(500),
            transactions = mutableListOf(BigDecimal(50), BigDecimal(20), BigDecimal(-15))
        )

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        val balance = viewBalanceUseCase.getBalance(1)

        assertEquals(BigDecimal(500), balance)
    }

    @Test
    fun testIfAccountNotFoundThrowException() {

        every { accountRepository.findById(1) } returns null

        assertFailsWith<IllegalArgumentException> {
            viewBalanceUseCase.getBalance(1)
        }
    }
}
