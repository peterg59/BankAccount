package com.example.bankAccount

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

fun main(args: Array<String>) {
    SpringApplication.run(BankAccountApplication::class.java, *args)
}

@SpringBootApplication
open class BankAccountApplication
