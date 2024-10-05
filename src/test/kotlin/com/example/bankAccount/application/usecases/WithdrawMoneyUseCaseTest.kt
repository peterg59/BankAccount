package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.AccountRepository
import com.example.bankAccount.domain.Operation
import com.example.bankAccount.domain.Transaction
import com.example.bankAccount.domain.exception.EmptyBalanceException
import com.example.bankAccount.domain.exception.InvalidAmountToWithdrawException
import com.example.bankAccount.domain.exception.InvalidIbanException
import io.mockk.*
import org.iban4j.Iban
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class WithdrawMoneyUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val withdrawMoneyUseCase = WithdrawMoneyUseCase(accountRepository)
    private val transaction1 = Transaction(id = 1, amount = BigDecimal(50), operation = Operation.DEPOSIT)
    private val transaction2 = Transaction(id = 2, amount = BigDecimal(80), operation = Operation.DEPOSIT)
    private val transaction3 = Transaction(id = 3, amount = BigDecimal(-80), operation = Operation.WITHDRAWAL)
    private val account = Account(
        iban = Iban.random().toString(),
        firstName = "John",
        lastName = "Doe",
        balance = BigDecimal(500),
        transactions = mutableListOf(transaction1, transaction2, transaction3)
    )

    @Test
    fun `Retrait d'un montant sur un compte, diminution du solde`() {

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        val updatedAccount = withdrawMoneyUseCase.withdrawMoney(account.iban, BigDecimal(50))
        verify { accountRepository.saveAccount(updatedAccount) }

        assertEquals(BigDecimal(450), updatedAccount.balance)
        verify { accountRepository.consultAccount(account.iban) }
        verify { accountRepository.saveAccount(any()) }
    }

    @Test
    fun `Mise a jour de la liste des transactions du compte`() {

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        val updatedAccount = withdrawMoneyUseCase.withdrawMoney(account.iban, BigDecimal(50))
        verify { accountRepository.saveAccount(updatedAccount) }

        val viewTransactionsUseCase = ViewTransactionsUseCase(accountRepository)
        val previousTransactions = viewTransactionsUseCase.getTransactions(account.iban)
        val transaction = Transaction(id = 0, amount = BigDecimal(-50), operation = Operation.WITHDRAWAL)

        assertEquals(4, previousTransactions.size)
        assertEquals(transaction.id, previousTransactions[3].id)
        assertEquals(transaction.amount, previousTransactions[3].amount)
        assertEquals(transaction.operation, previousTransactions[3].operation) 
        assertTrue(Duration.between(transaction.date, previousTransactions[3].date).abs().toMillis() < 1000)

        verify { accountRepository.consultAccount(account.iban) }
        verify { accountRepository.saveAccount(any()) }
    }

    @Test
    fun `Lorsque le numero du compte n'existe pas, alors la transaction echoue avec une InvalidIbanException`() {

        every { accountRepository.consultAccount(account.iban) } returns null

        assertFailsWith<InvalidIbanException> {
            withdrawMoneyUseCase.withdrawMoney(account.iban, BigDecimal(50))
        }
    }

    @Test
    fun `Lorsque le montant depose n'est pas valide, alors la transaction echoue avec une InvalidAmountToWithdrawException`() {

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        assertFailsWith<InvalidAmountToWithdrawException> {
            withdrawMoneyUseCase.withdrawMoney(account.iban, BigDecimal(550))
        }
    }

    @Test
    fun `Lorsque le solde est vide, alors la transaction echoue avec une InvalidAmountToDepositException`() {

        val account = Account(
            iban = Iban.random().toString(),
            firstName = "John",
            lastName = "Doe",
            balance = BigDecimal.ZERO,
            transactions = mutableListOf()
        )

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        assertFailsWith<EmptyBalanceException> {
            withdrawMoneyUseCase.withdrawMoney(account.iban, BigDecimal(50))
        }
    }
}