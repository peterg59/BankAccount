package com.example.bankAccount.application.usecases

import com.example.bankAccount.application.ports.out.AccountRepository
import com.example.bankAccount.model.Account
import io.mockk.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class ViewPreviousTransactionsUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val viewPreviousTransactionsUseCase = ViewPreviousTransactionsUseCase(accountRepository)
    private val depositMoneyUseCase = DepositMoneyUseCase(accountRepository)
    private val withdrawMoneyUseCase = WithdrawMoneyUseCase(accountRepository)

    @Test
    fun testViewPreviousTransactions() {

        val account = Account(
            id = 1,
            firstName = "Pierre",
            lastName = "Guyard",
            balance = 500.0,
            mapTransactions = linkedMapOf(1 to 50.0, 2 to 20.0, 3 to -15.0)
        )

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        val previousTransactions = viewPreviousTransactionsUseCase.getPreviousTransactions(1)

        assertEquals(3, previousTransactions.size)
        assertTrue(previousTransactions.containsKey(1))
        assertTrue(previousTransactions.containsKey(2))
        assertTrue(previousTransactions.containsKey(3))
    }

    @Test
    fun testIfAccountNotFoundThrowException() {

        every { accountRepository.findById(1) } returns null

        assertFailsWith<IllegalArgumentException> {
            viewPreviousTransactionsUseCase.getPreviousTransactions(1)
        }
    }

    @Test
    fun testIfDepositMadePreviousTransactionsUpdated() {

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

        val previousTransactions = viewPreviousTransactionsUseCase.getPreviousTransactions(1)

        assertEquals(4, previousTransactions.size)
        assertTrue(previousTransactions.containsKey(4))
        assertEquals(50.0, previousTransactions.getValue(4))
    }

    @Test
    fun testIfWithdrawMadePreviousTransactionsUpdated() {

        val account = Account(
            id = 1,
            firstName = "Pierre",
            lastName = "Guyard",
            balance = 500.0,
            mapTransactions = linkedMapOf(1 to 50.0, 2 to 20.0, 3 to -15.0)
        )

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        val mapTransactions = account.mapTransactions

        withdrawMoneyUseCase.withdrawMoney(1, 50.0)
        verify { accountRepository.save(account)}

        val previousTransactions = viewPreviousTransactionsUseCase.getPreviousTransactions(1)

        assertEquals(4, previousTransactions.size)
        assertTrue(previousTransactions.containsKey(4))
        assertEquals(-50.0, previousTransactions.getValue(4))
    }
}