package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.ports.`in`.ViewPreviousTransactionsInput
import com.example.bankAccount.domain.ports.out.AccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
open class ViewPreviousTransactionsUseCase(private val accountRepository: AccountRepository) :
    ViewPreviousTransactionsInput {

    override fun getPreviousTransactions(accountId: Long): List<BigDecimal> {
        val account = accountRepository.findById(accountId) ?: throw IllegalArgumentException("Account not found")

        return account.transactions
    }
}