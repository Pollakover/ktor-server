package com.example

import com.example.features.login.configureLoginRouting
import com.example.features.orders.configureOrdersRouting
import com.example.features.products.configureProductsRouting
import com.example.features.register.RegisterRouting
import com.example.features.suppliers.configureSuppliersRouting
import com.example.features.warehouses.configureWarehousesRouting
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
    configureProductsRouting()
    configureSuppliersRouting()
    configureWarehousesRouting()
    configureOrdersRouting()

    RegisterRouting().apply {
        this@module.configureRegisterRouting()
    }
}