package com.example

import com.example.login.configureLoginRouting
import com.example.register.RegisterRouting
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database


fun main() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/app",
        "org.postgresql.Driver",
        "postgres",
        "admin"
    )

    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureLoginRouting()

    RegisterRouting().apply {
        this@module.configureRegisterRouting()
    }
}