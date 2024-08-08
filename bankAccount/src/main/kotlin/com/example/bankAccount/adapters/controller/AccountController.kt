package com.example.bankAccount.adapters.controller

import com.example.bankAccount.domain.model.Account
import com.example.bankAccount.domain.ports.out.AccountRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(
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

    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable id: Long): ResponseEntity<Void> {
        accounts.delete(id)
        return ResponseEntity.ok().build()
    }
}