package com.example.bankAccount.application.usecases

import com.example.bankAccount.application.ports.`in`.WithdrawMoneyInput
import com.example.bankAccount.application.ports.out.AccountRepository

class WithdrawMoneyUseCase(private val accountRepository: AccountRepository) : WithdrawMoneyInput {

    override fun withdrawMoney(accountId: Long, amount: Double) {

        /** TODO */
    }
}