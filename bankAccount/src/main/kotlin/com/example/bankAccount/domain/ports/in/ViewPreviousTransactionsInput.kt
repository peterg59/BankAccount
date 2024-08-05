package com.example.bankAccount.domain.ports.`in`

interface ViewPreviousTransactionsInput {
    fun getPreviousTransactions(accountId: Long): LinkedHashMap<Int, Double>?
}