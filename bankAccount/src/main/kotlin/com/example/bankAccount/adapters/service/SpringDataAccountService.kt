package com.example.bankAccount.adapters.service

import com.example.bankAccount.adapters.persistence.*
import com.example.bankAccount.adapters.repository.SpringDataAccountRepository
import com.example.bankAccount.domain.model.Account
import com.example.bankAccount.domain.ports.out.AccountRepository
import org.springframework.stereotype.Service

@Service
class SpringDataAccountService(private val springDataAccountRepository: SpringDataAccountRepository) : AccountRepository {

    override fun findAll(): List<Account> {
        val accountList = springDataAccountRepository.findAll().map { accountEntity -> accountEntity.toDomain() }
        return accountList
    }

    override fun findById(accountId: Long): Account? {
        val account = springDataAccountRepository.findById(accountId).orElse(null)?.toDomain()
        return account
    }

    override fun save(account: Account) {
        val accountEntity = account.toEntity()
        springDataAccountRepository.save(accountEntity)
    }

    override fun delete(accountId: Long) {
        springDataAccountRepository.deleteById(accountId)
    }
}