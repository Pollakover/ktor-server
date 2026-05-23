package com.example.database.tokens

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.insert

object Tokens : Table("tokens") {
    private val login = Tokens.varchar("login",25)
    private val id = Tokens.varchar("id", 50)
    private val token = Tokens.varchar("token", 50)

    fun insert(tokenDTO : TokenDTO) {
        transaction {
            Tokens.insert{
                it[login] = tokenDTO.login
                it[id] = tokenDTO.rowId
                it[token] = tokenDTO.token
            }
        }
    }
}