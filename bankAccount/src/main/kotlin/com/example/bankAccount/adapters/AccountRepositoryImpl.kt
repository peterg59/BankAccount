package com.example.bankAccount.adapters

import com.example.bankAccount.application.ports.out.AccountRepository
import com.example.bankAccount.domain.Account

class AccountRepositoryImpl : AccountRepository {
    private val mapAccounts = mutableMapOf<Long, Account>()

    override fun findById(accountId: Long): Account? {
        /** TODO */
        return null
    }

    // Add or update an account
    override fun save(account: Account) {
        /** TODO */
    }

    override fun delete(accountId: Long) {
        /** TODO */
    }
}