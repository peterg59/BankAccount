package com.example.bankAccount.domain.ports.`in`

interface DepositMoneyInput {
    fun depositMoney(accountId: Long, amount: Double)
}