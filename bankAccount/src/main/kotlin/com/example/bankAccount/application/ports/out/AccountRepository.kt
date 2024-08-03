package com.example.bankAccount.application.ports.out

import com.example.bankAccount.model.Account

interface AccountRepository {
    fun findAll(): List<Account>
    fun findById(accountId: Long): Account?
    fun save(account: Account)
    fun delete(accountId: Long)
}
