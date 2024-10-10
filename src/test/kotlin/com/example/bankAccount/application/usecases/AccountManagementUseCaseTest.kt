package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.AccountRepository
import com.example.bankAccount.domain.Operation
import com.example.bankAccount.domain.Transaction
import io.mockk.*
import org.iban4j.Iban
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AccountManagementUseCaseTest {

    private val accountRepository = mockk<AccountRepository>()
    private val accountManagementUseCase = AccountManagementUseCase(accountRepository)
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
    fun `Ouverture d'un compte bancaire`() {

        every { accountRepository.openAccount(any()) } returns account.copy(
            balance = BigDecimal.ZERO,
            transactions = ArrayList()
        )

        val newAccount = NewAccount("John", "Doe")
        val accountOpened = accountManagementUseCase.openAccount(newAccount)

        assertEquals(account.iban, accountOpened.iban)
        assertEquals(account.firstName, accountOpened.firstName)
        assertEquals(account.lastName, accountOpened.lastName)
        assertEquals(BigDecimal.ZERO, accountOpened.balance)
        assertEquals(ArrayList(), accountOpened.transactions)
        verify { accountRepository.openAccount(any()) }
    }

    @Test
    fun `Consultation d'un compte bancaire`() {

        every { accountRepository.consultAccount(account.iban) } returns account

        val accountFound = accountManagementUseCase.consultAccount(account.iban)

        assertEquals(account, accountFound)
        verify { accountRepository.consultAccount(account.iban) }
    }

    @Test
    fun `Consultation de tous les comptes bancaires existants`() {

        val accountList = mutableListOf(
            Account(
                iban = Iban.random().toString(),
                firstName = "John",
                lastName = "Doe",
                balance = BigDecimal(500),
                transactions = mutableListOf()
            ), Account(
                iban = Iban.random().toString(),
                firstName = "Jane",
                lastName = "Doe",
                balance = BigDecimal(15000),
                transactions = mutableListOf()
            ), Account(
                iban = Iban.random().toString(),
                firstName = "Jean-Claude",
                lastName = "Bernard",
                balance = BigDecimal(6500),
                transactions = mutableListOf()
            )
        )
        every { accountRepository.consultAllAccounts() } returns accountList

        val accounts = accountManagementUseCase.consultAllAccounts()

        assertEquals(accountList, accounts)
        verify { accountRepository.consultAllAccounts() }
    }

    @Test
    fun `Fermeture d'un compte bancaire`() {

        every { accountRepository.consultAccount(account.iban) } returns null
        every { accountRepository.closeAccount(account.iban) } just Runs

        accountManagementUseCase.closeAccount(account.iban)
        val accountDeleted = accountManagementUseCase.consultAccount(account.iban)

        assertNull(accountDeleted)
        verify { accountRepository.closeAccount(account.iban) }
    }
}
