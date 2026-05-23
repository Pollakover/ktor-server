package com.example.database.users

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

object Users : Table("users") {

    val login = varchar("login", 25)
    val password = varchar("password", 25)
    //val username = varchar("username", 30)
    val email = varchar("email", 50)

    fun insert(userDTO: UserDTO) {
        transaction {
            insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[email] = userDTO.email ?: ""
            }

        }
    }

    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                Users.selectAll().where { Users.login eq login }.singleOrNull()?.let { row ->
                    UserDTO(
                        login = row[Users.login],
                        password = row[password],
                        //username = row[username],
                        email = row[email]
                    )
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}