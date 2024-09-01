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

class AccountControllerTest {

    private val accountManagementUseCase = mockk<AccountManagementUseCase>()
    private val accountController = AccountController(accountManagementUseCase)
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
    fun `Ouverture d'un compte bancaire, statut CREATED retourne`() {

        val newAccount = NewAccount("John", "Doe")
        every { accountManagementUseCase.openAccount(newAccount) } returns account

        val response = accountController.openAccount(newAccount)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals("/accounts/" + account.iban, response.headers.location.toString())
        verify { accountManagementUseCase.openAccount(newAccount) }
    }

    @Test
    fun `Consultation d'un compte bancaire, statut OK retourne`() {

        every { accountManagementUseCase.consultAccount(account.iban) } returns account

        val response = accountController.consultAccount(account.iban)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(account, response.body)
        verify { accountManagementUseCase.consultAccount(account.iban) }
    }

    @Test
    fun `Consultation de tous les comptes bancaires existants, statut OK retourne`() {

        val account1 = Account(
            iban = Iban.random().toString(),
            firstName = "John",
            lastName = "Doe",
            balance = BigDecimal(500),
            transactions = mutableListOf(transaction1)
        )

        val account2 = Account(
            iban = Iban.random().toString(),
            firstName = "Jane",
            lastName = "Doe",
            balance = BigDecimal(15000),
            transactions = mutableListOf(transaction2)
        )

        val account3 = Account(
            iban = Iban.random().toString(),
            firstName = "Jean-Claude",
            lastName = "Bernard",
            balance = BigDecimal(6500),
            transactions = mutableListOf(transaction3)
        )
        val accounts = mutableListOf(account1, account2, account3)
        every { accountManagementUseCase.consultAllAccounts() } returns accounts

        val response = accountController.consultAllAccounts()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(accounts, response.body)
        verify { accountManagementUseCase.consultAllAccounts() }
    }

    @Test
    fun `Consultation d'un compte bancaire inexistant, alors la consultation echoue, statut NOT_FOUND retourne`() {

        every { accountManagementUseCase.consultAccount(account.iban) } returns null

        val response = accountController.consultAccount(account.iban)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `Fermeture d'un compte bancaire, statut NO_CONTENT retourne`() {

        every { accountManagementUseCase.consultAccount(account.iban) } returns account
        every { accountManagementUseCase.closeAccount(any()) } just Runs

        val response = accountController.closeAccount(account.iban)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }
}