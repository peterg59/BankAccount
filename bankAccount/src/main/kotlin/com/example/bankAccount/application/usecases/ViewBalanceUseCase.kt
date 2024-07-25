package com.example.bankAccount.application.usecases

import com.example.bankAccount.application.ports.`in`.ViewBalanceInput
import com.example.bankAccount.application.ports.out.AccountRepository

class ViewBalanceUseCase(private val accountRepository: AccountRepository) : ViewBalanceInput {

    override fun getBalance(accountId: Long): Double? {
        /** TODO */

        return 0.0
    }
}