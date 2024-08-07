package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.ports.`in`.DepositMoneyInput
import com.example.bankAccount.domain.ports.out.AccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
open class DepositMoneyUseCase(private val accountRepository: AccountRepository) : DepositMoneyInput {

    override fun depositMoney(accountId: Long, amount: BigDecimal) {

        val account = accountRepository.findById(accountId) ?: throw IllegalArgumentException("Account not found")

        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Invalid amount to deposit")
        }

        // Update the balance with the amount deposited
        val updatedAccount = account.copy(balance = account.balance.add(amount))

        // Update the list of transactions
        updatedAccount.transactions.add(amount)
        accountRepository.save(updatedAccount)
    }
}