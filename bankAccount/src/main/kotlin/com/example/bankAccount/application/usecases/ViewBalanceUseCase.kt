package com.example.bankAccount.application.usecases

import com.example.bankAccount.application.ports.`in`.ViewBalanceInput
import com.example.bankAccount.application.ports.out.AccountRepository
import org.springframework.stereotype.Service

@Service
class ViewBalanceUseCase(private val accountRepository: AccountRepository) : ViewBalanceInput {

    override fun getBalance(accountId: Long): Double? {
        val account = accountRepository.findById(accountId)?: throw IllegalArgumentException("Account not found")

        return account.balance
    }
}