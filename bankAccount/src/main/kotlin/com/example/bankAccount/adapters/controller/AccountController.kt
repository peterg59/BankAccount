package com.example.bankAccount.adapters.controller

import com.example.bankAccount.adapters.dto.*
import com.example.bankAccount.domain.model.Account
import com.example.bankAccount.domain.ports.`in`.*
import com.example.bankAccount.domain.ports.out.AccountRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val depositMoneyUseCase: DepositMoneyInput,
    private val withdrawMoneyUseCase: WithdrawMoneyInput,
    private val viewBalanceUseCase: ViewBalanceInput,
    private val viewPreviousTransactionsUseCase: ViewPreviousTransactionsInput,
    private val accounts: AccountRepository
) {

    @GetMapping
    fun getAccounts(): ResponseEntity<List<Account>> {
        val accounts = accounts.findAll()
        return ResponseEntity.ok(accounts)
    }

    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: Long): ResponseEntity<Account> {
        val account = accounts.findById(id)
        return if (account != null) {
            ResponseEntity.ok(account)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun addAccount(@RequestBody account: Account): ResponseEntity<Void> {
        this.accounts.save(account)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{id}")
    fun updateAccount(@PathVariable id: Long, @RequestBody requestBody: Account): ResponseEntity<Account> {
        val account = accounts.findById(id) ?: return ResponseEntity.notFound().build()
        val updatedAccount = account.copy(
            firstName = requestBody.firstName,
            lastName = requestBody.lastName,
            balance = requestBody.balance,
            transactions = requestBody.transactions
        )

        this.accounts.save(updatedAccount)

        return ResponseEntity.ok(updatedAccount)
    }

    @PostMapping("/{id}/deposit")
    fun depositMoney(
        @PathVariable id: Long,
        @RequestBody depositMoneyRequest: DepositMoneyRequest
    ): ResponseEntity<Void> {
        depositMoneyUseCase.depositMoney(id, depositMoneyRequest.amount)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{id}/withdrawal")
    fun withdrawMoney(
        @PathVariable id: Long,
        @RequestBody withdrawMoneyRequest: WithdrawMoneyRequest
    ): ResponseEntity<Void> {
        withdrawMoneyUseCase.withdrawMoney(id, withdrawMoneyRequest.amount)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{id}/balance")
    fun viewBalance(@PathVariable id: Long): ResponseEntity<ViewBalanceResponse> {
        val balance = viewBalanceUseCase.getBalance(id)
        return if (balance != null)
            ResponseEntity.ok(ViewBalanceResponse(balance))
        else
            ResponseEntity.notFound().build()
    }

    @GetMapping("/{id}/transactions")
    fun viewPreviousTransactions(@PathVariable id: Long): ResponseEntity<ViewPreviousTransactionsResponse> {
        val previousTransactions = viewPreviousTransactionsUseCase.getPreviousTransactions(id)
        return if (previousTransactions != null)
            ResponseEntity.ok(ViewPreviousTransactionsResponse(previousTransactions))
        else
            ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable id: Long): ResponseEntity<Void> {
        accounts.delete(id)
        return ResponseEntity.ok().build()
    }
}