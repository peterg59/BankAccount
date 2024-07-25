package com.example.bankAccount.application.usecases

import com.example.bankAccount.application.ports.out.AccountRepository
import com.example.bankAccount.domain.Account
import io.mockk.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class DepositMoneyUseCaseTest { 

    private val accountRepository = mockk<AccountRepository>()
    private val depositMoneyUseCase = DepositMoneyUseCase(accountRepository)

    @Test
    fun testDepositMoney() {

        val account = Account(
            id = 1,
            firstName = "Pierre",
            lastName = "Guyard",
            balance = 500.0,
            mapTransactions = linkedMapOf(1 to 50.0, 2 to 20.0, 3 to -15.0)
        )

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        depositMoneyUseCase.depositMoney(1, 50.0)
        verify { accountRepository.save(account)}

        assertEquals(550.0, account.balance)
    }

    @Test
    fun testIfAccountNotFoundThrowException() {

        every { accountRepository.findById(1) } returns null

        assertFailsWith<IllegalArgumentException> {
            depositMoneyUseCase.depositMoney(1, 50.0)
        }
    }

    @Test
    fun testIfInvalidAmountToDepositThrowException () {

        val account = Account(
            id = 1,
            firstName = "Pierre",
            lastName = "Guyard",
            balance = 500.0,
            mapTransactions = linkedMapOf(1 to 50.0, 2 to 20.0, 3 to -15.0)
        )

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        assertFailsWith<IllegalArgumentException> {
            depositMoneyUseCase.depositMoney(1, 0.0)
        }
    }
}