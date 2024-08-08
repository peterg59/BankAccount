package com.example.bankAccount.adapters.controller

import com.example.bankAccount.adapters.dto.*
import com.example.bankAccount.domain.ports.`in`.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transactions/{accountId}")
class TransactionController(
    private val depositMoneyUseCase: DepositMoneyInput,
    private val withdrawMoneyUseCase: WithdrawMoneyInput,
    private val viewPreviousTransactionsUseCase: ViewPreviousTransactionsInput
) {

    @PostMapping("/deposit")
    fun depositMoney(
        @PathVariable accountId: Long,
        @RequestBody requestBody: DepositMoneyRequest
    ): ResponseEntity<Void> {
        depositMoneyUseCase.depositMoney(accountId, requestBody.amount)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/withdrawal")
    fun withdrawMoney(
        @PathVariable accountId: Long,
        @RequestBody requestBody: WithdrawMoneyRequest
    ): ResponseEntity<Void> {
        withdrawMoneyUseCase.withdrawMoney(accountId, requestBody.amount)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun viewPreviousTransactions(@PathVariable accountId: Long): ResponseEntity<ViewPreviousTransactionsResponse> {
        val transactions = viewPreviousTransactionsUseCase.getPreviousTransactions(accountId)
        return ResponseEntity.ok(ViewPreviousTransactionsResponse(transactions))
    }
}