package com.example.bankAccount

import com.example.bankAccount.adapters.controller.AccountController
import com.example.bankAccount.adapters.controller.DepositMoneyRequest
import com.example.bankAccount.adapters.controller.TransactionController
import com.example.bankAccount.adapters.controller.WithdrawMoneyRequest
import com.example.bankAccount.application.usecases.*
import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.Operation
import com.example.bankAccount.domain.Transaction
import com.example.bankAccount.domain.exception.EmptyBalanceException
import com.example.bankAccount.domain.exception.InvalidAmountToDepositException
import com.example.bankAccount.domain.exception.InvalidAmountToWithdrawException
import com.example.bankAccount.domain.exception.InvalidIbanException
import org.iban4j.Iban
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import kotlin.test.assertEquals

@SpringBootTest
class BankAccountApplicationTests {

    @MockBean
    lateinit var depositMoneyUseCase: DepositMoneyUseCase

    @MockBean
    lateinit var withdrawMoneyUseCase: WithdrawMoneyUseCase

    @MockBean
    lateinit var viewTransactionsUseCase: ViewTransactionsUseCase

    @MockBean
    lateinit var accountManagementUseCase: AccountManagementUseCase

    @Autowired
    lateinit var transactionController: TransactionController

    @Autowired
    lateinit var accountController: AccountController

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
    fun `Lancement de l'application`() {
    }

    @Test
    fun `Ouverture d'un compte bancaire, statut CREATED retourne`() {

        val newAccount = NewAccount("John", "Doe")
        val accountCreated = Account(
            iban = Iban.random().toString(),
            firstName = "John",
            lastName = "Doe",
            balance = BigDecimal.ZERO,
            transactions = mutableListOf()
        )

        // Stubber la méthode openAccount pour qu'elle retourne l'objet Account simulé
        whenever(accountManagementUseCase.openAccount(newAccount)).thenReturn(accountCreated)

        // Exécuter la méthode du contrôleur
        val response = accountController.openAccount(newAccount)

        // Vérifier que le statut de réponse est bien CREATED
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals("/accounts/" + accountCreated.iban, response.headers.location.toString())

        // Vérifier que la méthode openAccount a bien été appelée une fois avec les bons paramètres
        verify(accountManagementUseCase, times(1)).openAccount(newAccount)
    }

    @Test
    fun `Consultation d'un compte bancaire, statut OK retourne`() {

        whenever(accountManagementUseCase.consultAccount(account.iban)).thenReturn(account)

        val response = accountController.consultAccount(account.iban)

        assertEquals(HttpStatus.OK, response.statusCode)
        verify(accountManagementUseCase, times(1)).consultAccount(account.iban)
    }

    @Test
    fun `Consultation d'un compte bancaire inexistant, statut NOT_FOUND retourne`() {

        whenever(accountManagementUseCase.consultAccount(account.iban)).thenReturn(null)

        val response = accountController.consultAccount(account.iban)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        verify(accountManagementUseCase, times(1)).consultAccount(account.iban)
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

        whenever(accountManagementUseCase.consultAllAccounts()).thenReturn(accounts)

        val response = accountController.consultAllAccounts()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(accounts, response.body)
        verify(accountManagementUseCase, times(1)).consultAllAccounts()
    }

    @Test
    fun `Deposer un montant sur le compte bancaire, statut CREATED retourne`() {

        val amount = BigDecimal(100.00)

        whenever(depositMoneyUseCase.depositMoney(account.iban, amount)).thenReturn(account)

        val response = transactionController.depositMoney(account.iban, DepositMoneyRequest(amount))

        assertEquals(HttpStatus.CREATED, response.statusCode)

        verify(depositMoneyUseCase, times(1)).depositMoney(account.iban, amount)
    }


    @Test
    fun `Deposer un montant avec un IBAN errone, statut NOT_FOUND retourne`() {

        val invalidIban = "invalid-iban"
        val amount = BigDecimal(100.00)

        whenever(depositMoneyUseCase.depositMoney(invalidIban, amount)).thenAnswer {
            throw InvalidIbanException(invalidIban)
        }

        val response = transactionController.depositMoney(invalidIban, DepositMoneyRequest(amount))

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        verify(depositMoneyUseCase, times(1)).depositMoney(invalidIban, amount)
    }

    @Test
    fun `Deposer un montant invalide sur un compte bancaire, statut BAD_REQUEST retourne`() {

        val invalidAmount = BigDecimal.ZERO

        whenever(depositMoneyUseCase.depositMoney(account.iban, invalidAmount)).thenAnswer {
            throw InvalidAmountToDepositException(invalidAmount)
        }

        val response = transactionController.depositMoney(account.iban, DepositMoneyRequest(invalidAmount))

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        verify(depositMoneyUseCase, times(1)).depositMoney(account.iban, invalidAmount)
    }

    @Test
    fun `Retirer un montant sur le compte bancaire, statut CREATED retourne`() {

        val amount = BigDecimal("50.00")

        whenever(withdrawMoneyUseCase.withdrawMoney(account.iban, amount)).thenReturn(account)

        val response = transactionController.withdrawMoney(account.iban, WithdrawMoneyRequest(amount))

        assertEquals(HttpStatus.CREATED, response.statusCode)
        verify(withdrawMoneyUseCase, times(1)).withdrawMoney(account.iban, amount)
    }

    @Test
    fun `Retirer un montant d'un compte inexistant, statut NOT_FOUND retourne`() {

        val invalidIban = "invalid-iban"
        val amount = BigDecimal(100.00)

        whenever(withdrawMoneyUseCase.withdrawMoney(invalidIban, amount)).thenAnswer {
            throw InvalidIbanException(invalidIban)
        }

        val response = transactionController.withdrawMoney(invalidIban, WithdrawMoneyRequest(amount))

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        verify(withdrawMoneyUseCase, times(1)).withdrawMoney(invalidIban, amount)
    }

    @Test
    fun `Retirer un montant sur le compte bancaire sachant que le solde est vide, statut BAD_REQUEST retourne`() {

        val amount = BigDecimal("50.00")

        whenever(withdrawMoneyUseCase.withdrawMoney(account.iban, amount)).thenAnswer {
            throw EmptyBalanceException()
        }

        val response = transactionController.withdrawMoney(account.iban, WithdrawMoneyRequest(amount))

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        verify(withdrawMoneyUseCase, times(1)).withdrawMoney(account.iban, amount)
    }

    @Test
    fun `Retirer un montant invalide d'un compte bancaire, statut BAD_REQUEST retourne`() {

        val invalidAmount = BigDecimal.ZERO

        whenever(withdrawMoneyUseCase.withdrawMoney(account.iban, invalidAmount)).thenAnswer {
            throw InvalidAmountToWithdrawException(invalidAmount)
        }

        val response = transactionController.withdrawMoney(account.iban, WithdrawMoneyRequest(invalidAmount))

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        verify(withdrawMoneyUseCase, times(1)).withdrawMoney(account.iban, invalidAmount)
    }

    @Test
    fun `Consulter les transactions du compte bancaire, statut OK retourne`() {

        whenever(viewTransactionsUseCase.getTransactions(account.iban)).thenReturn(account.transactions)

        val response = transactionController.consultTransactions(account.iban)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(account.transactions, response.body?.transactions)
        verify(viewTransactionsUseCase, times(1)).getTransactions(account.iban)
    }

    @Test
    fun `Consulter les transactions d'un compte inexistant, alors la consultation echoue en renvoyant un statut NOT_FOUND`() {

        val invalidIban = "invalid-iban"

        whenever(viewTransactionsUseCase.getTransactions(invalidIban)).thenAnswer {
            throw InvalidIbanException(invalidIban)
        }

        val response = transactionController.consultTransactions(invalidIban)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        verify(viewTransactionsUseCase, times(1)).getTransactions(invalidIban)
    }

    @Test
    fun `Fermeture d'un compte bancaire, statut NO_CONTENT retourne`() {

        whenever(accountManagementUseCase.consultAccount(account.iban)).thenReturn(account)
        doNothing().`when`(accountManagementUseCase).closeAccount(account.iban)

        val response = accountController.closeAccount(account.iban)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(accountManagementUseCase, times(1)).closeAccount(account.iban)
    }
}