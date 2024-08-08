package com.example.bankAccount.domain.ports.`in`

import java.math.BigDecimal

interface ViewBalanceInput {
    fun getBalance(accountId: Long): BigDecimal
}