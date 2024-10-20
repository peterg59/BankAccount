package com.example.bankAccount.adapters.persistence

import com.example.bankAccount.domain.Operation
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "transaction")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    val id: Long,
    @Column(name = "account_iban", nullable = false)
    val accountIban: String,
    @Column(name = "transaction_date")
    val date: Instant,
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    val operation: Operation,
    @Column(name = "transaction_amount")
    val amount: BigDecimal
)