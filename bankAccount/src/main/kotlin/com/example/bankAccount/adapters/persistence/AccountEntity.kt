package com.example.bankAccount.adapters.persistence

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "account")
data class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    var firstName: String,
    var lastName: String,
    var balance: BigDecimal,
    var transactions: MutableList<BigDecimal>
) {

    // Constructeur par défaut requis par JPA
    constructor() : this(0, "", "", BigDecimal.ZERO, mutableListOf())
}