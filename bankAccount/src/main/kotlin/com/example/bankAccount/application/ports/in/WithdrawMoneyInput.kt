package com.example.bankAccount.application.ports.`in`

interface WithdrawMoneyInput {
    fun withdrawMoney(accountId: Long, amount: Double)
}