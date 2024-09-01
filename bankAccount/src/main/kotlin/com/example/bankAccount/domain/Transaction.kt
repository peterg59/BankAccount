package com.example.bankAccount.domain

import java.math.BigDecimal
import java.time.LocalDate

data class Transaction(
    val id: Long,
    val date: String = LocalDate.now().toString(),
    val operation: Operation,
    val amount: BigDecimal
)