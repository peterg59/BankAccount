package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.ports.out.AccountRepository
import com.example.bankAccount.domain.model.Account
import com.example.bankAccount.application.usecases.WithdrawMoneyUseCase
import io.mockk.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class WithdrawMoneyUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val withdrawMoneyUseCase = WithdrawMoneyUseCase(accountRepository)

    @Test
    fun testWithdrawMoney() {

        val account = Account(
            id = 1,
            firstName = "Pierre",
            lastName = "Guyard",
            balance = 500.0,
            mapTransactions = linkedMapOf(1 to 50.0, 2 to 20.0, 3 to -15.0)
        )

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        withdrawMoneyUseCase.withdrawMoney(1, 50.0)
        verify { accountRepository.save(account) }

        assertEquals(450.0, account.balance)
    }

    @Test
    fun testIfAccountNotFoundThrowException() {

        every { accountRepository.findById(1) } returns null

        assertFailsWith<IllegalArgumentException> {
            withdrawMoneyUseCase.withdrawMoney(1, 50.0)
        }
    }

    @Test
    fun testIfInvalidAmountToWithdrawThrowException() {

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
            withdrawMoneyUseCase.withdrawMoney(1, 550.0)
        }
    }

    @Test
    fun ifTheBalanceIsEmptyThrowException() {

        val account = Account(
            id = 1,
            firstName = "Pierre",
            lastName = "Guyard",
            balance = 0.0,
            mapTransactions = linkedMapOf(1 to 50.0, 2 to 20.0, 3 to -15.0)
        )

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        assertFailsWith<IllegalArgumentException> {
            withdrawMoneyUseCase.withdrawMoney(1, 50.0)
        }
    }
}