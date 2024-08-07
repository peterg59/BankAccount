package com.example.bankAccount.adapters.dto

import java.math.BigDecimal

data class WithdrawMoneyRequest(val accountId: Long, val amount: BigDecimal)