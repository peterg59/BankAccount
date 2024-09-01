package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.*
import com.example.bankAccount.domain.exception.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
open class WithdrawMoneyUseCase(private val accountRepository: AccountRepository) {

    /**
     * Retrait d'argent sur un compte dédié
     *
     * @throws InvalidIbanException en cas de compte inexistant
     * @throws InvalidAmountToWithdrawException en cas de montant invalide
     * @throws EmptyBalanceException en cas de solde vide
     */
    fun withdrawMoney(iban: String, amount: BigDecimal): Account {

        val account = accountRepository.consultAccount(iban)
            ?: throw InvalidIbanException(iban)

        val currentAccountBalance = account.balance

        if (currentAccountBalance <= BigDecimal.ZERO) {
            throw EmptyBalanceException()
        }

        if (amount <= BigDecimal.ZERO || amount > currentAccountBalance) {
            throw InvalidAmountToWithdrawException(amount)
        }

        // Update the balance with the amount withdrawal
        val updatedAccount = account.copy(balance = account.balance - amount)
        val transaction = Transaction(id = 0L, operation = Operation.WITHDRAWAL, amount = amount.negate())

        // Update the list of transactions
        updatedAccount.transactions.add(transaction)
        accountRepository.saveAccount(updatedAccount)
        return updatedAccount
    }
}