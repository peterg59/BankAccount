package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.ports.out.AccountRepository
import com.example.bankAccount.domain.model.Account
import io.mockk.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.*

class DepositMoneyUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val depositMoneyUseCase = DepositMoneyUseCase(accountRepository)
    private val account = Account(
        id = 1,
        firstName = "Pierre",
        lastName = "Guyard",
        balance = BigDecimal(500),
        transactions = mutableListOf(BigDecimal(50), BigDecimal(20), BigDecimal(-15))
    )

    @Test
    fun testDepositMoney() {

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        val updatedAccount = depositMoneyUseCase.depositMoney(1, BigDecimal(50))
        verify { accountRepository.save(updatedAccount) }

        assertEquals(BigDecimal(550), updatedAccount.balance)
    }

    @Test
    fun testIfAccountNotFoundThrowException() {

        every { accountRepository.findById(1) } returns null

        assertFailsWith<IllegalArgumentException> {
            depositMoneyUseCase.depositMoney(1, BigDecimal(50))
        }
    }

    @Test
    fun testIfInvalidAmountToDepositThrowException() {

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        assertFailsWith<IllegalArgumentException> {
            depositMoneyUseCase.depositMoney(1, BigDecimal.ZERO)
        }
    }
}