package com.example.bankAccount.domain.exception

import java.lang.Exception
import java.math.BigDecimal

class InvalidAmountToDepositException(val amount: BigDecimal): Exception()