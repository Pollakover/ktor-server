package com.example

import com.example.features.files.configureFilesRouting
import com.example.features.login.configureLoginRouting
import com.example.features.orders.configureOrdersRouting
import com.example.features.products.configureProductsRouting
import com.example.features.register.RegisterRouting
import com.example.features.suppliers.configureSuppliersRouting
import com.example.features.warehouses.configureWarehousesRouting
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.v1.jdbc.Database


fun main() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/app",
        "org.postgresql.Driver",
        "postgres",
        "1234"
    )

    embeddedServer(CIO, port = 8080, host = "10.220.43.227",  module = Application::module)
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
    configureFilesRouting()

    RegisterRouting().apply {
        this@module.configureRegisterRouting()
    }
}