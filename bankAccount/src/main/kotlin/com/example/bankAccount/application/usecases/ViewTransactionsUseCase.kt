package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.AccountRepository
import com.example.bankAccount.domain.Transaction
import com.example.bankAccount.domain.exception.InvalidIbanException
import org.springframework.stereotype.Service

@Service
open class ViewTransactionsUseCase(private val accountRepository: AccountRepository) {

    /**
     * Consultation des transactions d'un compte bancaire
     *
     * @throws InvalidIbanException en cas de compte inexistant
     */
    fun getTransactions(iban: String): List<Transaction> {
        val account = accountRepository.consultAccount(iban)
            ?: throw InvalidIbanException(iban)

        return account.transactions
    }
}