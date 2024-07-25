package com.example.bankAccount.application.ports.out

import com.example.bankAccount.domain.Account

interface AccountRepository {
    fun findById(accountId: Long): Account?
    fun save(account: Account)
    fun delete(accountId: Long)
}
