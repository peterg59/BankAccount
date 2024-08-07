package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.ports.`in`.WithdrawMoneyInput
import com.example.bankAccount.domain.ports.out.AccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
open class WithdrawMoneyUseCase(private val accountRepository: AccountRepository) : WithdrawMoneyInput {

    override fun withdrawMoney(accountId: Long, amount: BigDecimal) {

        val account = accountRepository.findById(accountId) ?: throw IllegalArgumentException("Account not found")
        val currentAccountBalance = account.balance

        if (currentAccountBalance <= BigDecimal.ZERO) {
            throw IllegalArgumentException("The balance is empty, cannot withdraw")
        }

        if (amount <= BigDecimal.ZERO || amount > currentAccountBalance) {
            throw IllegalArgumentException("Invalid amount to withdraw")
        }

        // Update the balance with the amount withdrawal
        val accountCopy = account.copy(balance = account.balance.subtract(amount))

        // Update the list of transactions
        accountCopy.transactions.add(amount.negate())
        accountRepository.save(accountCopy)
    }
}