package com.example.bankAccount.controller

import com.example.bankAccount.adapters.SpringDataAccountRepository
import org.springframework.web.bind.annotation.*

@RestController
class AccountController(private val springDataAccountRepository: SpringDataAccountRepository) {

}