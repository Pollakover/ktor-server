package com.example.features.login

import com.example.database.tokens.TokenDTO
import com.example.database.tokens.Tokens
import com.example.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import java.util.UUID
import io.ktor.server.response.*

class LoginController(private val call:ApplicationCall) {
    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if (userDTO.password == receive.password) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(
                        rowId = UUID.randomUUID().toString(),
                        login = receive.login,
                        token = token
                    )
                )
                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }

    suspend fun getUserByLogin() {
        try {
            val request = call.receive<GetUserByLoginRequest>()
            val userDTO = Users.fetchUser(request.login)

            if (userDTO != null) {
                call.respond(HttpStatusCode.OK, mapOf(
                    "login" to userDTO.login,
                    "username" to userDTO.username,
                    "email" to userDTO.email
                ))
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }
}