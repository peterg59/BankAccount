package com.example.bankAccount.model

import jakarta.persistence.*

@Entity
data class Account(@Id
                   @GeneratedValue(strategy = GenerationType.AUTO)
                   val id: Long
                   , val firstName: String, val lastName: String, var balance: Double
                   , var mapTransactions: LinkedHashMap<Int, Double>) {

}