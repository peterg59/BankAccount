package com.example.bankAccount.adapters.repository

import com.example.bankAccount.adapters.persistence.AccountEntity
import com.example.bankAccount.adapters.persistence.TransactionEntity
import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.AccountRepository
import com.example.bankAccount.domain.Transaction
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
open class SpringDataAccountRepositoryAdapter(private val springDataAccountRepository: SpringDataAccountRepository) :
    AccountRepository {

    override fun consultAllAccounts(): List<Account> {
        val accountList = springDataAccountRepository.findAll().map { it.toDomain() }
        return accountList
    }

    override fun consultAccount(iban: String): Account? {
        val account = springDataAccountRepository.findByIban(iban)?.toDomain()
        return account
    }

    override fun openAccount(account: Account): Account {
        val accountEntity = account.toEntity()
        springDataAccountRepository.save(accountEntity)
        return account
    }

    override fun updateAccount(account: Account) {
        val accountEntity = account.toEntity()
        springDataAccountRepository.save(accountEntity)
    }

    override fun closeAccount(iban: String) {
        springDataAccountRepository.deleteById(iban)
    }
}

// Mappage de AccountEntity à Account
private fun AccountEntity.toDomain(): Account {
    return Account(
        iban = this.iban,
        firstName = this.firstName,
        lastName = this.lastName,
        balance = this.balance,
        transactions = this.transactions.map { it.toDomain() }.toMutableList()
    )
}

// Mappage de Account à AccountEntity
private fun Account.toEntity(): AccountEntity {
    val accountEntity = AccountEntity(
        iban = this.iban,
        firstName = this.firstName,
        lastName = this.lastName,
        balance = this.balance,
        transactions = this.transactions.map { it.toEntity(this) }
            .toMutableList()
    )
    return accountEntity
}

// Mappage de TransactionEntity à Transaction
private fun TransactionEntity.toDomain(): Transaction {

    return Transaction(
        id = this.id,
        date = this.date,
        operation = this.operation,
        amount = this.amount
    )
}

// Mappage de Transaction à TransactionEntity
private fun Transaction.toEntity(account: Account): TransactionEntity {

    return TransactionEntity(
        id = this.id,
        date = this.date,
        operation = this.operation,
        amount = this.amount,
        accountIban = account.iban
    )
}