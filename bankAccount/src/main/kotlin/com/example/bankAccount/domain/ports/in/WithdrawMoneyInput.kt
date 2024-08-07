package com.example.bankAccount.domain.ports.`in`

import java.math.BigDecimal

interface WithdrawMoneyInput {
    fun withdrawMoney(accountId: Long, amount: BigDecimal)
}