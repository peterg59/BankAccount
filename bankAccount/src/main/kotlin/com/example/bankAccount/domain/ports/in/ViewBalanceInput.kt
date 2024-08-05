package com.example.bankAccount.domain.ports.`in`

interface ViewBalanceInput {
    fun getBalance(accountId: Long): Double?
}