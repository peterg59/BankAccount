package com.example.bankAccount.adapters.repository

import com.example.bankAccount.adapters.persistence.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataAccountRepository : JpaRepository<AccountEntity, String> {
    fun findByIban(iban: String): AccountEntity?
}