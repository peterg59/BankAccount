package com.example.bankAccount.adapters.repository

import com.example.bankAccount.adapters.persistence.*
import com.example.bankAccount.domain.model.Account
import com.example.bankAccount.domain.ports.out.AccountRepository
import org.springframework.stereotype.Repository

@Repository
open class SpringDataAccountRepositoryAdapter(private val springDataAccountRepository: SpringDataAccountRepository) :
    AccountRepository {

    override fun findAll(): List<Account> {
        val accountList = springDataAccountRepository.findAll().map { accountEntity -> accountEntity.toDomain() }
        return accountList
    }

    override fun findById(accountId: Long): Account? {
        val account = springDataAccountRepository.findAccountById(accountId)?.toDomain()
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

private fun AccountEntity.toDomain() = Account(id, firstName, lastName, balance, transactions)
private fun Account.toEntity() = AccountEntity(id, firstName, lastName, balance, transactions)