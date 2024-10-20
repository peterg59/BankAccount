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
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ViewTransactionsUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val viewTransactionsUseCase = ViewTransactionsUseCase(accountRepository)
    private val fixedInstant = Instant.parse("2024-10-05T12:34:56Z")
    private val account = Account(
        iban = Iban.random().toString(),
        firstName = "John",
        lastName = "Doe",
        balance = BigDecimal(500),
        transactions = mutableListOf(
            Transaction(id = 1, amount = BigDecimal(50), operation = Operation.DEPOSIT, date = fixedInstant),
            Transaction(id = 2, amount = BigDecimal(80), operation = Operation.DEPOSIT, date = fixedInstant),
            Transaction(id = 3, amount = BigDecimal(-80), operation = Operation.WITHDRAWAL, date = fixedInstant)
        )
    )

    @Test
    fun `Consultation des transactions d'un compte`() {

        every { accountRepository.consultAccount(account.iban) } returns account

        val resultInstant = Instant.parse("2024-10-05T12:34:56Z")
        val transactions = viewTransactionsUseCase.getTransactions(account.iban)

        assertEquals(3, transactions.size)
        assertEquals(
            Transaction(id = 1, amount = BigDecimal(50), operation = Operation.DEPOSIT, date = resultInstant),
            transactions[0]
        )
        assertEquals(
            Transaction(id = 2, amount = BigDecimal(80), operation = Operation.DEPOSIT, date = resultInstant),
            transactions[1]
        )
        assertEquals(
            Transaction(
                id = 3,
                amount = BigDecimal(-80),
                operation = Operation.WITHDRAWAL,
                date = resultInstant
            ), transactions[2]
        )
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
