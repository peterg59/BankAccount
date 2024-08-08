package com.example.bankAccount.adapters.controller

import com.example.bankAccount.adapters.dto.*
import com.example.bankAccount.domain.ports.`in`.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/balance/{accountId}")
class BalanceController(
    private val viewBalanceUseCase: ViewBalanceInput
) {

    @GetMapping
    fun viewBalance(@PathVariable accountId: Long): ResponseEntity<ViewBalanceResponse> {
        val balance = viewBalanceUseCase.getBalance(accountId)
        return ResponseEntity.ok(ViewBalanceResponse(balance))
    }
}