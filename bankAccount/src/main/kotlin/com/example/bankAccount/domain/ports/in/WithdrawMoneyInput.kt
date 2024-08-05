package com.example.bankAccount.domain.ports.`in`

interface WithdrawMoneyInput {
    fun withdrawMoney(accountId: Long, amount: Double)
}