package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.AccountRepository
import org.springframework.stereotype.Service

@Service
open class AccountManagementUseCase(private val accountRepository: AccountRepository) {

    fun openAccount(newAccount: NewAccount): Account {

        val accountOpened = Account(firstName = newAccount.firstName, lastName = newAccount.lastName)
        return accountRepository.openAccount(accountOpened)
    }

    fun consultAccount(iban: String): Account? {
        return accountRepository.consultAccount(iban)
    }

    fun consultAllAccounts(): List<Account> {
        return accountRepository.consultAllAccounts()
    }

    fun closeAccount(iban: String) {
        accountRepository.closeAccount(iban)
    }
}

data class NewAccount(val firstName: String, val lastName: String)