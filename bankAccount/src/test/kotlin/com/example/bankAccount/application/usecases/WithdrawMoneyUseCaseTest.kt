package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.ports.out.AccountRepository
import com.example.bankAccount.domain.model.Account
import io.mockk.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.*

class WithdrawMoneyUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val withdrawMoneyUseCase = WithdrawMoneyUseCase(accountRepository)
    private val account = Account(
        id = 1,
        firstName = "Pierre",
        lastName = "Guyard",
        balance = BigDecimal(500),
        transactions = mutableListOf(BigDecimal(50), BigDecimal(20), BigDecimal(-15))
    )

    @Test
    fun testWithdrawMoney() {

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        withdrawMoneyUseCase.withdrawMoney(1, BigDecimal(50))
        verify { accountRepository.save(account) }

        assertEquals(BigDecimal(450), account.balance)
    }

    @Test
    fun testIfAccountNotFoundThrowException() {

        every { accountRepository.findById(1) } returns null

        assertFailsWith<IllegalArgumentException> {
            withdrawMoneyUseCase.withdrawMoney(1, BigDecimal(50))
        }
    }

    @Test
    fun testIfInvalidAmountToWithdrawThrowException() {

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        assertFailsWith<IllegalArgumentException> {
            withdrawMoneyUseCase.withdrawMoney(1, BigDecimal(550))
        }
    }

    @Test
    fun ifTheBalanceIsEmptyThrowException() {

        val account = Account(
            id = 1,
            firstName = "Pierre",
            lastName = "Guyard",
            balance = BigDecimal.ZERO,
            transactions = mutableListOf(BigDecimal(50), BigDecimal(20), BigDecimal(-15))
        )

        every { accountRepository.findById(1) } returns account
        every { accountRepository.save(any()) } just Runs

        assertFailsWith<IllegalArgumentException> {
            withdrawMoneyUseCase.withdrawMoney(1, BigDecimal(50))
        }
    }
}