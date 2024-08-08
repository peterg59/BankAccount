package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.ports.out.AccountRepository
import com.example.bankAccount.domain.model.Account
import io.mockk.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.*

class ViewPreviousTransactionsUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val viewPreviousTransactionsUseCase = ViewPreviousTransactionsUseCase(accountRepository)
    private val depositMoneyUseCase = DepositMoneyUseCase(accountRepository)
    private val withdrawMoneyUseCase = WithdrawMoneyUseCase(accountRepository)
    private val account = Account(
        id = 1,
        firstName = "Pierre",
        lastName = "Guyard",
        balance = BigDecimal(500),
        transactions = mutableListOf(BigDecimal(50), BigDecimal(20), BigDecimal(-15))
    )

    @Test
    fun testViewPreviousTransactions() {

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        val previousTransactions = viewPreviousTransactionsUseCase.getPreviousTransactions(1)

        assertEquals(3, previousTransactions.size)
        assertEquals(BigDecimal(50), previousTransactions[0])
        assertEquals(BigDecimal(20), previousTransactions[1])
        assertEquals(BigDecimal(-15), previousTransactions[2])
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

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        val updatedAccount = depositMoneyUseCase.depositMoney(1, BigDecimal(50))
        verify { accountRepository.save(updatedAccount) }

        val previousTransactions = viewPreviousTransactionsUseCase.getPreviousTransactions(1)

        assertEquals(4, previousTransactions.size)
        assertEquals(BigDecimal(50), previousTransactions[3])
    }

    @Test
    fun testIfWithdrawMadePreviousTransactionsUpdated() {

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        val updatedAccount = withdrawMoneyUseCase.withdrawMoney(1, BigDecimal(50))
        verify { accountRepository.save(updatedAccount) }

        val previousTransactions = viewPreviousTransactionsUseCase.getPreviousTransactions(1)

        assertEquals(4, previousTransactions.size)
        assertEquals(BigDecimal(-50), previousTransactions[3])
    }
}