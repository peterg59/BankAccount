package com.example.bankAccount.domain.ports.`in`

import com.example.bankAccount.domain.model.Account
import java.math.BigDecimal

interface WithdrawMoneyInput {
    fun withdrawMoney(accountId: Long, amount: BigDecimal): Account
}