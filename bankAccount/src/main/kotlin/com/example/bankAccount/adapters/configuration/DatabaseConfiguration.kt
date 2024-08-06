package com.example.bankAccount.adapters.configuration

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.*
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
@EnableAutoConfiguration
open class DatabaseConfiguration {

    @Bean
    open fun initializeDatabase(jdbcTemplate: JdbcTemplate) = ApplicationRunner {
        jdbcTemplate.execute("ALTER TABLE account ALTER COLUMN map_transactions SET DATA TYPE VARBINARY(2048)")
    }
}