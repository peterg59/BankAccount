package com.example.bankAccount.domain

import java.math.BigDecimal
import java.time.Instant

data class Transaction(
    val id: Long,
    val date: Instant,
    val operation: Operation,
    val amount: BigDecimal
)
