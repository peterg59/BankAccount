package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.AccountRepository
import com.example.bankAccount.domain.Operation
import com.example.bankAccount.domain.Transaction
import com.example.bankAccount.domain.exception.InvalidIbanException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.iban4j.Iban
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ViewTransactionsUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val viewTransactionsUseCase = ViewTransactionsUseCase(accountRepository)
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
    fun `Consultation des transactions d'un compte`() {

        every { accountRepository.consultAccount(account.iban) } returns account

        val transactions = viewTransactionsUseCase.getTransactions(account.iban)

        assertEquals(3, transactions.size)
        assertEquals(transaction1, transactions[0])
        assertEquals(transaction2, transactions[1])
        assertEquals(transaction3, transactions[2])
        verify { accountRepository.consultAccount(account.iban) }
    }

    @Test
    fun `Lorsque le numero du compte n'existe pas, alors la consultation echoue avec une InvalidIbanException`() {

        every { accountRepository.consultAccount(account.iban) } returns null

        assertFailsWith<InvalidIbanException> {
            viewTransactionsUseCase.getTransactions(account.iban)
        }
    }
}