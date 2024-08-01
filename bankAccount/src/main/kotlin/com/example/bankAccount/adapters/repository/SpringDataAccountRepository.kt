package com.example.bankAccount.adapters.repository

import com.example.bankAccount.application.ports.out.AccountRepository
import com.example.bankAccount.domain.Account
import org.springframework.stereotype.Repository

<<<<<<<< HEAD:bankAccount/src/main/kotlin/com/example/bankAccount/adapters/repository/SpringDataAccountRepository.kt
@Repository
class SpringDataAccountRepository : AccountRepository {
========
class InMemoryAccountRepository : AccountRepository {
>>>>>>>> develop:bankAccount/src/main/kotlin/com/example/bankAccount/adapters/InMemoryAccountRepository.kt
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