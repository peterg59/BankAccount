package com.example.bankAccount.adapters.persistence

import jakarta.persistence.*

@Entity
@Table(name = "Account")
data class AccountEntity(@Id
                    @GeneratedValue(strategy = GenerationType.AUTO)
                    val id: Long
                    , val firstName: String, val lastName: String, var balance: Double
                    , var mapTransactions: LinkedHashMap<Int, Double>)