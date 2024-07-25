package com.example.bankAccount.domain

data class Account(val id: Long, val firstName: String, val lastName: String,
                   var balance: Double, var mapTransactions: LinkedHashMap<Int, Double>) {

}