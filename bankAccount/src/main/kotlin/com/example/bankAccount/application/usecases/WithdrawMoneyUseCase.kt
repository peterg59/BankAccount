package com.example.bankAccount.application.usecases

import com.example.bankAccount.application.ports.`in`.WithdrawMoneyInput
import com.example.bankAccount.application.ports.out.AccountRepository
import org.springframework.stereotype.Service

@Service
class WithdrawMoneyUseCase(private val accountRepository: AccountRepository) : WithdrawMoneyInput {

    override fun withdrawMoney(accountId: Long, amount: Double) {

        val account = accountRepository.findById(accountId)?: throw IllegalArgumentException("Account not found")
        var currentAccountBalance = account.balance

        if(currentAccountBalance <= 0) {
            throw IllegalArgumentException("The balance is empty, cannot withdraw")
        }

        if(amount <= 0 || amount > currentAccountBalance) {
            throw IllegalArgumentException("Invalid amount to withdraw")
        }

        // Update the balance with the amount withdrawled
        account.balance -= amount

        // Update the map of transactions
        var lastKey = account.mapTransactions.keys.last()
        account.mapTransactions[++lastKey] = -amount
        accountRepository.save(account)
    }
}