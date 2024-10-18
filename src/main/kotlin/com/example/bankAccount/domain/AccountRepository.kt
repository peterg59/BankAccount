package com.example.bankAccount.domain

interface AccountRepository {
    fun consultAllAccounts(): List<Account>
    fun consultAccount(iban: String): Account?
    fun openAccount(account: Account): Account
    fun saveAccount(account: Account)
    fun closeAccount(iban: String)
}
