package com.example.bankAccount.domain

import org.iban4j.Iban
import java.math.BigDecimal

data class Account(
    val iban: String = Iban.random().toString(),
    val firstName: String,
    val lastName: String,
    val balance: BigDecimal = BigDecimal.ZERO,
    val transactions: MutableList<Transaction> = ArrayList()
)