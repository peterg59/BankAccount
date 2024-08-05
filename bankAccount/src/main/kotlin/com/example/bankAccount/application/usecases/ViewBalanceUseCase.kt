package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.ports.`in`.ViewBalanceInput
import com.example.bankAccount.domain.ports.out.AccountRepository
import org.springframework.stereotype.Service

@Service
open class ViewBalanceUseCase(private val accountRepository: AccountRepository) : ViewBalanceInput {

    override fun getBalance(accountId: Long): Double? {
        val account = accountRepository.findById(accountId)?: throw IllegalArgumentException("Account not found")

        return account.balance
    }
}