package com.example.bankAccount.adapters.dto

import java.math.BigDecimal

data class DepositMoneyRequest(val accountId: Long, val amount: BigDecimal)