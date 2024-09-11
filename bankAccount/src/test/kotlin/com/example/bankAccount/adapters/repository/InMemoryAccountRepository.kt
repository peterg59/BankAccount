package com.example.bankAccount.adapters.repository

import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.AccountRepository
import java.math.BigDecimal

class InMemoryAccountRepository() : AccountRepository {

    private val mapAccounts = mutableMapOf<String, Account>()

    override fun consultAllAccounts(): List<Account> {
        return mapAccounts.values.toList()
    }

    override fun consultAccount(iban: String): Account? {
        return mapAccounts[iban]
    }

    override fun openAccount(account: Account): Account {
        val accountOpened = account.copy(balance = BigDecimal.ZERO, transactions = ArrayList())
        mapAccounts[accountOpened.iban] = accountOpened
        return accountOpened
    }

    // Update an account
    override fun updateAccount(account: Account) {
        mapAccounts[account.iban] = account
    }

    override fun closeAccount(iban: String) {
        mapAccounts.remove(iban)
    }
}