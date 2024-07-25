package com.example.bankAccount.application.ports.`in`

interface ViewPreviousTransactionsInput {
    fun getPreviousTransactions(accountId: Long): LinkedHashMap<Int, Double>?
}