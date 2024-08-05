package com.example.bankAccount.adapters.persistence

import com.example.bankAccount.domain.model.Account

    fun AccountEntity.toDomain() = Account(id, firstName, lastName, balance, mapTransactions)
    fun Account.toEntity() = AccountEntity(id, firstName, lastName, balance, mapTransactions)