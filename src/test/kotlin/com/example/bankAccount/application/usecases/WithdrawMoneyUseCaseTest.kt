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
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WithdrawMoneyUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val withdrawMoneyUseCase = WithdrawMoneyUseCase(accountRepository)
    private val date = Clock.systemUTC().instant()
    private val account = Account(
        iban = Iban.random().toString(),
        firstName = "John",
        lastName = "Doe",
        balance = BigDecimal(500),
        transactions = listOf(
            Transaction(id = 1, amount = BigDecimal(50), operation = Operation.DEPOSIT, date = date),
            Transaction(id = 2, amount = BigDecimal(80), operation = Operation.DEPOSIT, date = date),
            Transaction(id = 3, amount = BigDecimal(-80), operation = Operation.WITHDRAWAL, date = date)
        )
    )

    @Test
    fun `Retrait d'un montant sur un compte, diminution du solde`() {

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        val updatedAccount = withdrawMoneyUseCase.withdrawMoney(account.iban, BigDecimal(50), Clock.systemUTC())
        verify { accountRepository.saveAccount(updatedAccount) }

        assertEquals(BigDecimal(450), updatedAccount.balance)
        verify { accountRepository.consultAccount(account.iban) }
        verify { accountRepository.saveAccount(any()) }
    }

    @Test
    fun `Mise a jour de la liste des transactions du compte`() {

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        // Définir une heure fixe pour le test
        val fixedInstant = Instant.parse("2024-10-05T12:30:00Z")
        val clock = Clock.fixed(fixedInstant, ZoneOffset.UTC)

        val updatedAccount = withdrawMoneyUseCase.withdrawMoney(account.iban, BigDecimal(50), clock)
        verify { accountRepository.saveAccount(updatedAccount) }

        val transactions = updatedAccount.transactions
        val transaction =
            Transaction(
                id = 0L,
                amount = BigDecimal(-50),
                operation = Operation.WITHDRAWAL,
                date = fixedInstant
            )

        assertEquals(4, transactions.size)
        assertEquals(transaction.id, transactions.last().id)
        assertEquals(transaction.amount, transactions.last().amount)
        assertEquals(transaction.operation, transactions.last().operation)
        assertEquals(transaction.date, transactions.last().date)

        verify { accountRepository.saveAccount(any()) }
    }

    @Test
    fun `Lorsque le numero du compte n'existe pas, alors la transaction echoue avec une InvalidIbanException`() {

        every { accountRepository.consultAccount(account.iban) } returns null

        assertFailsWith<InvalidIbanException> {
            withdrawMoneyUseCase.withdrawMoney(account.iban, BigDecimal(50), Clock.systemUTC())
        }
    }

    @Test
    fun `Lorsque le montant depose n'est pas valide, alors la transaction echoue avec une InvalidAmountToWithdrawException`() {

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        assertFailsWith<InvalidAmountToWithdrawException> {
            withdrawMoneyUseCase.withdrawMoney(account.iban, BigDecimal(550), Clock.systemUTC())
        }
    }

    @Test
    fun `Lorsque le solde est vide, alors la transaction echoue avec une InvalidAmountToDepositException`() {

        val account = Account(
            iban = Iban.random().toString(),
            firstName = "John",
            lastName = "Doe",
            balance = BigDecimal.ZERO,
            transactions = listOf()
        )

        every { accountRepository.consultAccount(account.iban) } returns account
        every { accountRepository.saveAccount(any()) } just Runs

        assertFailsWith<EmptyBalanceException> {
            withdrawMoneyUseCase.withdrawMoney(account.iban, BigDecimal(50), Clock.systemUTC())
        }
    }
}
