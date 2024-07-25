package com.example.bankAccount.application.ports.`in`

interface ViewBalanceInput {
    fun getBalance(accountId: Long): Double?
}