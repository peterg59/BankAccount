package com.example.bankAccount.adapters.controller

import com.example.bankAccount.application.usecases.DepositMoneyUseCase
import com.example.bankAccount.application.usecases.ViewTransactionsUseCase
import com.example.bankAccount.application.usecases.WithdrawMoneyUseCase
import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.Operation
import com.example.bankAccount.domain.Transaction
import com.example.bankAccount.domain.exception.EmptyBalanceException
import com.example.bankAccount.domain.exception.InvalidAmountToDepositException
import com.example.bankAccount.domain.exception.InvalidAmountToWithdrawException
import com.example.bankAccount.domain.exception.InvalidIbanException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.iban4j.Iban
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal

class TransactionControllerTest {

    private val depositMoneyUseCase = mockk<DepositMoneyUseCase>()
    private val withdrawMoneyUseCase = mockk<WithdrawMoneyUseCase>()
    private val viewTransactionsUseCase = mockk<ViewTransactionsUseCase>()
    private val transactionController =
        TransactionController(depositMoneyUseCase, withdrawMoneyUseCase, viewTransactionsUseCase)
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
    private val amount = BigDecimal(50)

    @Test
    fun `Deposer un montant sur le compte bancaire, augmentation du solde, statut CREATED retourne`() {

        every { depositMoneyUseCase.depositMoney(any(), any()) } returns account


        val response = transactionController.depositMoney(account.iban, DepositMoneyRequest(amount))

        assertEquals(HttpStatus.CREATED, response.statusCode)
        verify { depositMoneyUseCase.depositMoney(account.iban, amount) }
    }

    @Test
    fun `Retirer un montant du compte bancaire, diminution du solde, statut CREATED retourne`() {

        every { withdrawMoneyUseCase.withdrawMoney(any(), any()) } returns account

        val response = transactionController.withdrawMoney(account.iban, WithdrawMoneyRequest(amount))

        assertEquals(HttpStatus.CREATED, response.statusCode)
        verify { withdrawMoneyUseCase.withdrawMoney(account.iban, amount) }
    }

    @Test
    fun `Consulter les transactions du compte bancaire, statut OK retourne`() {

        every { viewTransactionsUseCase.getTransactions(any()) } returns account.transactions

        val response = transactionController.consultTransactions(account.iban)

        assertEquals(HttpStatus.OK, response.statusCode)
        verify { viewTransactionsUseCase.getTransactions(account.iban) }
    }

    @Test
    fun `Deposer un montant sur un compte inexistant, alors la transaction echoue en renvoyant un statut NOT_FOUND`() {

        every { depositMoneyUseCase.depositMoney(any(), any()) } throws InvalidIbanException(account.iban)

        val response = transactionController.depositMoney(account.iban, DepositMoneyRequest(amount))

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `Deposer un montant invalide sur un compte bancaire, alors la transaction echoue en renvoyant un statut BAD_REQUEST`() {

        val invalidAmount = BigDecimal.ZERO

        every { depositMoneyUseCase.depositMoney(any(), any()) } throws InvalidAmountToDepositException(invalidAmount)

        val response = transactionController.depositMoney(account.iban, DepositMoneyRequest(invalidAmount))

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `Retirer un montant d'un compte inexistant, alors la transaction echoue en renvoyant un statut NOT_FOUND`() {

        every { withdrawMoneyUseCase.withdrawMoney(any(), any()) } throws InvalidIbanException(account.iban)

        val response = transactionController.withdrawMoney(account.iban, WithdrawMoneyRequest(amount))

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `Retirer un montant invalide d'un compte bancaire, alors la transaction echoue en renvoyant un statut BAD_REQUEST`() {

        every { withdrawMoneyUseCase.withdrawMoney(any(), any()) } throws InvalidAmountToWithdrawException(amount)

        val response = transactionController.withdrawMoney(account.iban, WithdrawMoneyRequest(amount))

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `Retirer un montant sur un solde vide, alors la transaction echoue en renvoyant un statut BAD_REQUEST`() {

        every { withdrawMoneyUseCase.withdrawMoney(any(), any()) } throws EmptyBalanceException()

        val response = transactionController.withdrawMoney(account.iban, WithdrawMoneyRequest(amount))

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `Consulter les transactions d'un compte inexistant, alors la consultation echoue en renvoyant un statut NOT_FOUND`() {

        every { viewTransactionsUseCase.getTransactions(any()) } throws InvalidIbanException(account.iban)

        val response = transactionController.consultTransactions(account.iban)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}