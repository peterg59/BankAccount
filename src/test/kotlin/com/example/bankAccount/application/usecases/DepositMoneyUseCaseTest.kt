package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.AccountRepository
import com.example.bankAccount.domain.Operation
import com.example.bankAccount.domain.Transaction
import com.example.bankAccount.domain.exception.InvalidAmountToDepositException
import com.example.bankAccount.domain.exception.InvalidIbanException
import io.mockk.*
import org.iban4j.Iban
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DepositMoneyUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val depositMoneyUseCase = DepositMoneyUseCase(accountRepository)
    private val account = Account(
        iban = Iban.random().toString(),
        firstName = "John",
        lastName = "Doe",
        balance = BigDecimal(500),
        transactions = mutableListOf(
            Transaction(id = 1, amount = BigDecimal(50), operation = Operation.DEPOSIT),
            Transaction(id = 2, amount = BigDecimal(80), operation = Operation.DEPOSIT),
            Transaction(id = 3, amount = BigDecimal(-80), operation = Operation.WITHDRAWAL)
        )
    )

    @Test
    fun `Depot d'un montant sur un compte, augmentation du solde`() {

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        val updatedAccount = depositMoneyUseCase.depositMoney(account.iban, BigDecimal(50))
        verify { accountRepository.saveAccount(updatedAccount) }

        assertEquals(BigDecimal(550), updatedAccount.balance)
        verify { accountRepository.consultAccount(account.iban) }
        verify { accountRepository.saveAccount(any()) }
    }

    @Test
    fun `Mise a jour de la liste des transactions du compte`() {

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        val updatedAccount = depositMoneyUseCase.depositMoney(account.iban, BigDecimal(50))
        verify { accountRepository.saveAccount(updatedAccount) }

        val transactions = updatedAccount.transactions
        val transaction =
            Transaction(
                id = 0L,
                amount = BigDecimal(50),
                operation = Operation.DEPOSIT
            )

        assertEquals(4, transactions.size)
        assertEquals(transaction.id, transactions[3].id)
        assertEquals(transaction.amount, transactions[3].amount)
        assertEquals(transaction.operation, transactions[3].operation)
        assertTrue(Duration.between(transaction.date, transactions[3].date).abs().toMillis() < 1000)

        verify { accountRepository.saveAccount(any()) }
    }

    @Test
    fun `Lorsque le numero du compte n'existe pas, alors la transaction echoue avec une InvalidIbanException`() {

        every { accountRepository.consultAccount(account.iban) } returns null

        assertFailsWith<InvalidIbanException> {
            depositMoneyUseCase.depositMoney(account.iban, BigDecimal(50))
        }
    }

    @Test
    fun `Lorsque le montant depose n'est pas valide, alors la transaction echoue avec une InvalidAmountToDepositException`() {

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        assertFailsWith<InvalidAmountToDepositException> {
            depositMoneyUseCase.depositMoney(account.iban, BigDecimal.ZERO)
        }
    }
}
