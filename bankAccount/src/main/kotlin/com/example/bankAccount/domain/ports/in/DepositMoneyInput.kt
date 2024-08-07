package com.example.bankAccount.domain.ports.`in`

import java.math.BigDecimal

interface DepositMoneyInput {
    fun depositMoney(accountId: Long, amount: BigDecimal)
}