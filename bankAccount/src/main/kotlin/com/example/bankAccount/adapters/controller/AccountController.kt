package com.example.bankAccount.adapters.controller

import com.example.bankAccount.application.usecases.DepositMoneyUseCase
import com.example.bankAccount.application.usecases.ViewBalanceUseCase
import com.example.bankAccount.application.usecases.ViewPreviousTransactionsUseCase
import com.example.bankAccount.application.usecases.WithdrawMoneyUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AccountController(
    private val depositMoneyUseCase: DepositMoneyUseCase,
    private val withdrawMoneyUseCase: WithdrawMoneyUseCase,
    private val viewBalanceUseCase: ViewBalanceUseCase,
    private val viewPreviousTransactionsUseCase: ViewPreviousTransactionsUseCase) {

    @RequestMapping(method = [RequestMethod.POST], value = ["/accounts/{id}/deposit"])
    fun depositMoney(@PathVariable id: Long, @RequestBody depositMoneyRequest: DepositMoneyRequest ): ResponseEntity<Void> {
        depositMoneyUseCase.depositMoney(id, depositMoneyRequest.amount)
        return ResponseEntity.ok().build()
    }

    @RequestMapping(method = [RequestMethod.POST], value = ["/accounts/{id}/withdraw"])
    fun withdrawMoney(@PathVariable id: Long, @RequestBody withdrawMoneyRequest: WithdrawMoneyRequest): ResponseEntity<Void> {
        withdrawMoneyUseCase.withdrawMoney(id, withdrawMoneyRequest.amount)
        return ResponseEntity.ok().build()
    }

    @RequestMapping(method = [RequestMethod.GET], value = ["/accounts/{id}/balance"])
    fun viewBalance(@PathVariable id: Long): ResponseEntity<ViewBalanceResponse> {
        val balance = viewBalanceUseCase.getBalance(id)
        return if(balance != null)
            ResponseEntity.ok(ViewBalanceResponse(balance))
        else
            ResponseEntity.notFound().build()
    }

    @RequestMapping(method = [RequestMethod.GET], value = ["/accounts/{id}/previousTransactions"])
    fun viewPreviousTransactions(@PathVariable id: Long): ResponseEntity<ViewPreviousTransactionsResponse> {
        val previousTransactions = viewPreviousTransactionsUseCase.getPreviousTransactions(id)
        return if(previousTransactions != null)
            ResponseEntity.ok(ViewPreviousTransactionsResponse(previousTransactions))
        else
            ResponseEntity.notFound().build()
    }

    data class ViewPreviousTransactionsResponse(var mapTransactions: LinkedHashMap<Int, Double>)
    data class ViewBalanceResponse(var balance: Double)
    data class DepositMoneyRequest(val accountId: Long, val amount: Double)
    data class WithdrawMoneyRequest(val accountId: Long, val amount: Double)
}