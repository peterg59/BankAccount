package com.example.bankAccount.domain.model

data class Account(val id: Long
                   , var firstName: String, var lastName: String, var balance: Double
                   , var mapTransactions: LinkedHashMap<Int, Double>)