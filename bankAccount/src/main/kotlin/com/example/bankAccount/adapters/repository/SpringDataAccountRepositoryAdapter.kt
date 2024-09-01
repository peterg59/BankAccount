package com.example.bankAccount.adapters.repository

import com.example.bankAccount.adapters.persistence.AccountEntity
import com.example.bankAccount.adapters.persistence.TransactionEntity
import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.AccountRepository
import com.example.bankAccount.domain.Transaction
import org.springframework.stereotype.Repository
import java.text.SimpleDateFormat

@Repository
open class SpringDataAccountRepositoryAdapter(private val springDataAccountRepository: SpringDataAccountRepository) :
    AccountRepository {

    override fun consultAllAccounts(): List<Account> {
        val accountList = springDataAccountRepository.findAll().map { accountEntity -> accountEntity.toDomain() }
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

    override fun saveAccount(account: Account) {
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
        transactions = this.transactions.map { transactionEntity -> transactionEntity.toDomain() }.toMutableList()
    )
}

// Mappage de Account à AccountEntity
private fun Account.toEntity(): AccountEntity {
    val accountEntity = AccountEntity(
        iban = this.iban,
        firstName = this.firstName,
        lastName = this.lastName,
        balance = this.balance,
        transactions = this.transactions.map { transaction -> transaction.toEntity(this) }
            .toMutableList()  // Mappage des transactions
    )
    return accountEntity
}

// Mappage de TransactionEntity à Transaction
private fun TransactionEntity.toDomain(): Transaction {

    // Conversion de Date à String
    val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    val dateString = formatter.format(this.date)

    return Transaction(
        id = this.id,
        date = dateString,
        operation = this.operation,
        amount = this.amount
    )
}

// Mappage de Transaction à TransactionEntity
private fun Transaction.toEntity(account: Account): TransactionEntity {

    // Conversion de String à Date
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = formatter.parse(this.date)

    return TransactionEntity(
        id = this.id,
        date = date,
        operation = this.operation,
        amount = this.amount,
        accountIban = account.iban  // Associer la transaction au compte
    )
}