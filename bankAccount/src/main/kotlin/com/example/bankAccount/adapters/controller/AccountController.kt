package com.example.bankAccount.adapters.controller

import com.example.bankAccount.adapters.dto.*
import com.example.bankAccount.adapters.service.SpringDataAccountService
import com.example.bankAccount.application.usecases.*
import com.example.bankAccount.domain.model.Account
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AccountController(
    private val depositMoneyUseCase: DepositMoneyUseCase,
    private val withdrawMoneyUseCase: WithdrawMoneyUseCase,
    private val viewBalanceUseCase: ViewBalanceUseCase,
    private val viewPreviousTransactionsUseCase: ViewPreviousTransactionsUseCase,
    private val springDataAccountService: SpringDataAccountService
) {

    @RequestMapping(method = [RequestMethod.GET], value = ["/accounts"])
    fun getAccounts(): ResponseEntity<List<Account>> {
        val accounts = springDataAccountService.findAll()
        return ResponseEntity.ok(accounts)
    }

    @RequestMapping(method = [RequestMethod.GET], value = ["/accounts/{id}"])
    fun getAccount(@PathVariable id: Long): ResponseEntity<Account>{
        val account = springDataAccountService.findById(id)
        return if (account != null) {
            ResponseEntity.ok(account)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @RequestMapping(method = [RequestMethod.POST], value = ["/accounts"])
    fun addAccount(@RequestBody account: Account): ResponseEntity<Void> {
        springDataAccountService.save(account)
        return ResponseEntity.ok().build()
    }

    @RequestMapping(method = [RequestMethod.PUT], value = ["/accounts/{id}"])
    fun updateAccount(@RequestBody account: Account, @PathVariable id: Long): ResponseEntity<Void> {
        val accountInDB = springDataAccountService.findById(id)

        if(accountInDB != null){
            if(account.firstName != null)
                accountInDB.firstName = account.firstName
            if(account.lastName != null)
                accountInDB.lastName = account.lastName

            accountInDB.balance = account.balance

            if(account.mapTransactions != null)
                accountInDB.mapTransactions = account.mapTransactions

            springDataAccountService.save(accountInDB)
        }
        return ResponseEntity.ok().build()
    }

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

    @RequestMapping(method = [RequestMethod.DELETE], value = ["/accounts/{id}"])
    fun deleteAccount(@PathVariable id: Long): ResponseEntity<Void> {
        springDataAccountService.delete(id)
        return ResponseEntity.ok().build()
    }
}