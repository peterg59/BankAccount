package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.ports.`in`.DepositMoneyInput
import com.example.bankAccount.domain.ports.out.AccountRepository
import org.springframework.stereotype.Service

@Service
open class DepositMoneyUseCase(private val accountRepository: AccountRepository) : DepositMoneyInput {

    override fun depositMoney(accountId: Long, amount: Double) {

        val account = accountRepository.findById(accountId)?: throw IllegalArgumentException("Account not found")

        if(amount <= 0) {
            throw IllegalArgumentException("Invalid amount to deposit")
        }

        // Update the balance with the amount deposited
        account.balance += amount

        // Update the map of transactions
        var lastKey = account.mapTransactions.keys.last()
        account.mapTransactions[++lastKey] = amount
        accountRepository.save(account)
    }
}