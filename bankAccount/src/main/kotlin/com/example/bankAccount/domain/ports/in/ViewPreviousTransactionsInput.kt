package com.example.bankAccount.domain.ports.`in`

import java.math.BigDecimal

interface ViewPreviousTransactionsInput {
    fun getPreviousTransactions(accountId: Long): List<BigDecimal>
}