package com.example.bankAccount.domain.model

import java.math.BigDecimal

data class Account(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val balance: BigDecimal,
    val transactions: MutableList<BigDecimal>
)