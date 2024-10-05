package com.example.bankAccount.adapters.persistence

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "account")
data class AccountEntity(
    @Id
    @Column(name = "iban", unique = true, nullable = false)
    val iban: String,
    @Column(name = "first_name")
    var firstName: String,
    @Column(name = "last_name")
    var lastName: String,
    @Column(name = "balance")
    var balance: BigDecimal,
    @Column(name = "transactions")
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "account_iban")
    var transactions: MutableList<TransactionEntity>
)