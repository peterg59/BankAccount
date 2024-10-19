package com.example.bankAccount.application.usecases

import com.example.bankAccount.domain.Account
import com.example.bankAccount.domain.AccountRepository
import com.example.bankAccount.domain.Operation
import com.example.bankAccount.domain.Transaction
import com.example.bankAccount.domain.exception.EmptyBalanceException
import com.example.bankAccount.domain.exception.InvalidAmountToWithdrawException
import com.example.bankAccount.domain.exception.InvalidIbanException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant

@Service
open class WithdrawMoneyUseCase(private val accountRepository: AccountRepository) {

    /**
     * Retrait d'argent sur un compte dédié.
     *
     * @param iban l'iban du compte bancaire
     * @param amount le montant retiré
     * @param clock la periode de la transaction
     *
     * @throws InvalidIbanException en cas de compte inexistant
     * @throws InvalidAmountToWithdrawException en cas de montant invalide
     * @throws EmptyBalanceException en cas de solde vide
     *
     * @return le compte bancaire modifié
     */
    fun withdrawMoney(iban: String, amount: BigDecimal, clock: Clock): Account {

        val account = accountRepository.consultAccount(iban)
            ?: throw InvalidIbanException(iban)

        val currentAccountBalance = account.balance

        if (currentAccountBalance <= BigDecimal.ZERO) {
            throw EmptyBalanceException()
        }

        if (amount <= BigDecimal.ZERO || amount > currentAccountBalance) {
            throw InvalidAmountToWithdrawException(amount)
        }

        // Update the balance with the amount withdrawal and the list of transactions too
        val transaction = Transaction(
            id = 0L,
            operation = Operation.WITHDRAWAL,
            amount = amount.negate(),
            date = Instant.now(clock)
        )
        val updatedAccount =
            account.copy(balance = account.balance - amount, transactions = account.transactions + transaction)

        accountRepository.saveAccount(updatedAccount)
        return updatedAccount
    }
}
