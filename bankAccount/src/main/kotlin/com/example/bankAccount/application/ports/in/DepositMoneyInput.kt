package com.example.bankAccount.application.ports.`in`

interface DepositMoneyInput {
    fun depositMoney(accountId: Long, amount: Double)
}