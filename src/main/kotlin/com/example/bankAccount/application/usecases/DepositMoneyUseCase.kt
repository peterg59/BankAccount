package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.AccountRepository
import com.example.bankAccount.domain.Operation
import com.example.bankAccount.domain.Transaction
import com.example.bankAccount.domain.exception.InvalidAmountToDepositException
import com.example.bankAccount.domain.exception.InvalidIbanException
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
open class DepositMoneyUseCase(private val accountRepository: AccountRepository) {

    /**
     * Dépot d'argent sur un compte bancaire dédié.
     *
     * @param iban l'iban du compte bancaire
     * @param amount le montant déposé
     *
     * @throws InvalidIbanException en cas de compte inexistant
     * @throws InvalidAmountToDepositException en cas de montant invalide
     *
     * @return le compte bancaire modifié
     */
    fun depositMoney(iban: String, amount: BigDecimal): Account {

        val account = accountRepository.consultAccount(iban)
            ?: throw InvalidIbanException(iban)

        if (amount <= BigDecimal.ZERO) {
            throw InvalidAmountToDepositException(amount)
        }

        // Update the balance with the amount deposited and the list of transactions too
        val transaction = Transaction(id = 0L, operation = Operation.DEPOSIT, amount = amount)
        val updatedAccount = account.copy(balance = account.balance + amount, transactions = account.transactions + transaction)

        accountRepository.saveAccount(updatedAccount)
        return updatedAccount
    }
}
