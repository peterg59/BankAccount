package com.example.bankAccount.adapters.persistence

import jakarta.persistence.*

@Entity
@Table(name = "Account")
data class AccountEntity(@Id
                    @GeneratedValue(strategy = GenerationType.AUTO)
                    val id: Long
                    , var firstName: String, var lastName: String, var balance: Double
                    , var mapTransactions: LinkedHashMap<Int, Double>) {

    // Constructeur par défaut requis par JPA
    constructor() : this(0, "", "", 0.0, linkedMapOf())
}