package com.example.bankAccount.application.usecases

import com.example.bankAccount.application.ports.`in`.ViewPreviousTransactionsInput
import com.example.bankAccount.application.ports.out.AccountRepository
import org.springframework.stereotype.Service

@Service
class ViewPreviousTransactionsUseCase(private val accountRepository: AccountRepository) :
    ViewPreviousTransactionsInput {

    override fun getPreviousTransactions(accountId: Long): LinkedHashMap<Int, Double> {
        val account = accountRepository.findById(accountId)?: throw IllegalArgumentException("Account not found")

        return account.mapTransactions
    }
}