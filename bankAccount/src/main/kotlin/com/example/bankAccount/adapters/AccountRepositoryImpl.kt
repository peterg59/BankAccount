package com.example.bankAccount.adapters

import com.example.bankAccount.application.ports.out.AccountRepository
import com.example.bankAccount.domain.Account

class AccountRepositoryImpl : AccountRepository {
    private val mapAccounts = mutableMapOf<Long, Account>()

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