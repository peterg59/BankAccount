package com.example.bankAccount.application.ports.out

import com.example.bankAccount.domain.Account
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository {
    fun findById(accountId: Long): Account?
    fun save(account: Account)
    fun delete(accountId: Long)
}
