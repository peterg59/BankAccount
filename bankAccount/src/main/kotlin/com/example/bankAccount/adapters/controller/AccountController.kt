package com.example.bankAccount.adapters.controller

import com.example.bankAccount.application.usecases.AccountManagementUseCase
import com.example.bankAccount.application.usecases.NewAccount
import com.example.bankAccount.domain.Account
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountManagementUseCase: AccountManagementUseCase,
) {

    @GetMapping
    fun consultAllAccounts(): ResponseEntity<List<Account>> {
        val accounts = accountManagementUseCase.consultAllAccounts()
        return ResponseEntity.ok(accounts)
    }

    @GetMapping("/{iban}")
    fun consultAccount(@PathVariable iban: String): ResponseEntity<Account> {
        val account = accountManagementUseCase.consultAccount(iban)
        return if (account != null) {
            ResponseEntity.ok(account)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun openAccount(@RequestBody newAccount: NewAccount): ResponseEntity<Unit> {
        val accountOpened = accountManagementUseCase.openAccount(newAccount)
        val location = URI.create("/accounts/${accountOpened.iban}")
        return ResponseEntity.created(location).build()
    }

    @DeleteMapping("/{iban}")
    fun closeAccount(@PathVariable iban: String): ResponseEntity<Unit> {
        val account = accountManagementUseCase.consultAccount(iban)
        return if (account != null) {
            accountManagementUseCase.closeAccount(iban)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}