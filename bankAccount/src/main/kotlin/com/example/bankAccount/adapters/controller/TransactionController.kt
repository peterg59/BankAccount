package com.example.bankAccount.adapters.controller

import com.example.bankAccount.application.usecases.DepositMoneyUseCase
import com.example.bankAccount.application.usecases.ViewTransactionsUseCase
import com.example.bankAccount.application.usecases.WithdrawMoneyUseCase
import com.example.bankAccount.domain.Transaction
import com.example.bankAccount.domain.exception.EmptyBalanceException
import com.example.bankAccount.domain.exception.InvalidAmountToDepositException
import com.example.bankAccount.domain.exception.InvalidAmountToWithdrawException
import com.example.bankAccount.domain.exception.InvalidIbanException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("accounts/{iban}")
class TransactionController(
    private val depositMoneyUseCase: DepositMoneyUseCase,
    private val withdrawMoneyUseCase: WithdrawMoneyUseCase,
    private val viewTransactionsUseCase: ViewTransactionsUseCase
) {

    @PostMapping("/deposits")
    fun depositMoney(
        @PathVariable iban: String,
        @RequestBody requestBody: DepositMoneyRequest
    ): ResponseEntity<Unit> {
        try {
            depositMoneyUseCase.depositMoney(iban, requestBody.amount)
        } catch (invalidIban: InvalidIbanException) {
            return ResponseEntity.notFound().build()
        } catch (invalidAmountToDeposit: InvalidAmountToDepositException) {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/withdrawals")
    fun withdrawMoney(
        @PathVariable iban: String,
        @RequestBody requestBody: WithdrawMoneyRequest
    ): ResponseEntity<Unit> {
        try {
            withdrawMoneyUseCase.withdrawMoney(iban, requestBody.amount)
        } catch (invalidIban: InvalidIbanException) {
            return ResponseEntity.notFound().build()
        } catch (invalidAmountToWithdraw: InvalidAmountToWithdrawException) {
            return ResponseEntity.badRequest().build()
        } catch (emptyBalance: EmptyBalanceException) {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/transactions")
    fun consultTransactions(@PathVariable iban: String): ResponseEntity<ViewTransactionsResponse> {
        val transactions = try {
            viewTransactionsUseCase.getTransactions(iban)
        } catch (invalidIban: InvalidIbanException) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(ViewTransactionsResponse(transactions))
    }
}

data class WithdrawMoneyRequest(val amount: BigDecimal)

data class DepositMoneyRequest(val amount: BigDecimal)

data class ViewTransactionsResponse(val transactions: List<Transaction>)