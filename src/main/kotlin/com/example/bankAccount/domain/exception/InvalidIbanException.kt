package com.example.bankAccount.domain.exception

import java.lang.Exception

class InvalidIbanException(val iban: String): Exception()