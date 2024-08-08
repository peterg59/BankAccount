package com.example.bankAccount.adapters.repository

import com.example.bankAccount.adapters.persistence.AccountEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataAccountRepository : CrudRepository<AccountEntity, Long> {
    fun findAccountById(accountId: Long): AccountEntity?
}