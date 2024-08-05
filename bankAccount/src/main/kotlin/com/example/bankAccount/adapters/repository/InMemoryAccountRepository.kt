package com.example.bankAccount.adapters.repository

import com.example.bankAccount.domain.ports.out.AccountRepository
import com.example.bankAccount.domain.model.Account

class InMemoryAccountRepository() : AccountRepository {

    private val mapAccounts = mutableMapOf<Long, Account>()

    override fun findAll(): List<Account> {
        return mapAccounts.values.toList()
    }

    override fun findById(accountId: Long): Account? {
        return mapAccounts[accountId]
    }

    // Add or update an account
    override fun save(account: Account) {
        mapAccounts[account.id] = account
    }

    override fun delete(accountId: Long) {
        mapAccounts.remove(accountId)
    }
}