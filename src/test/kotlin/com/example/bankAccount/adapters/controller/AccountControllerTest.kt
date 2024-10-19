package com.example.bankAccount.adapters.controller

import com.example.bankAccount.application.usecases.AccountManagementUseCase
import com.example.bankAccount.application.usecases.NewAccount
import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.Operation
import com.example.bankAccount.domain.Transaction
import io.mockk.*
import org.iban4j.Iban
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant

class AccountControllerTest {

    private val accountManagementUseCase = mockk<AccountManagementUseCase>()
    private val accountController = AccountController(accountManagementUseCase)
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
    fun `Ouverture d'un compte bancaire`() {

        val newAccount = NewAccount("John", "Doe")
        every { accountManagementUseCase.openAccount(newAccount) } returns account

        val response = accountController.openAccount(newAccount)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals("/accounts/" + account.iban, response.headers.location.toString())
        verify { accountManagementUseCase.openAccount(newAccount) }
    }

    @Test
    fun `Consultation d'un compte bancaire`() {

        every { accountManagementUseCase.consultAccount(account.iban) } returns account

        val response = accountController.consultAccount(account.iban)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(account, response.body)
        verify { accountManagementUseCase.consultAccount(account.iban) }
    }

    @Test
    fun `Consultation de tous les comptes bancaires existants`() {

        val accounts = listOf(
            Account(
                iban = Iban.random().toString(),
                firstName = "John",
                lastName = "Doe",
                balance = BigDecimal(500),
                transactions = listOf(
                    Transaction(
                        id = 1,
                        amount = BigDecimal(50),
                        operation = Operation.DEPOSIT,
                        date = Instant.now()
                    )
                )
            ), Account(
                iban = Iban.random().toString(),
                firstName = "Jane",
                lastName = "Doe",
                balance = BigDecimal(15000),
                transactions = listOf(
                    Transaction(
                        id = 2,
                        amount = BigDecimal(80),
                        operation = Operation.DEPOSIT,
                        date = Instant.now()
                    )
                )
            ), Account(
                iban = Iban.random().toString(),
                firstName = "Jean-Claude",
                lastName = "Bernard",
                balance = BigDecimal(6500),
                transactions = listOf(
                    Transaction(
                        id = 3,
                        amount = BigDecimal(-80),
                        operation = Operation.WITHDRAWAL,
                        date = Instant.now()
                    )
                )
            )
        )
        every { accountManagementUseCase.consultAllAccounts() } returns accounts

        val response = accountController.consultAllAccounts()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(accounts, response.body)
        verify { accountManagementUseCase.consultAllAccounts() }
    }

    @Test
    fun `Consultation d'un compte bancaire inexistant, alors la consultation echoue`() {

        every { accountManagementUseCase.consultAccount(account.iban) } returns null

        val response = accountController.consultAccount(account.iban)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `Fermeture d'un compte bancaire`() {

        every { accountManagementUseCase.consultAccount(account.iban) } returns account
        every { accountManagementUseCase.closeAccount(any()) } just Runs

        val response = accountController.closeAccount(account.iban)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }
}
