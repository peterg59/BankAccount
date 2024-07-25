package com.example.bankAccount.application.usecases

import com.example.bankAccount.application.ports.`in`.DepositMoneyInput
import com.example.bankAccount.application.ports.out.AccountRepository

class DepositMoneyUseCase(private val accountRepository: AccountRepository) : DepositMoneyInput {

    override fun depositMoney(accountId: Long, amount: Double) {
        /** TODO */
    }
}