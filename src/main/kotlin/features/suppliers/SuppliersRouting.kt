package com.example.features.suppliers

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSuppliersRouting() {
    routing {
        post("/suppliers/fetch") {
            val suppliersController = SuppliersController(call)
            suppliersController.fetchSuppliers()
        }

        post("/suppliers/add") {
            val suppliersController = SuppliersController(call)
            suppliersController.addSupplier()
        }

        post("/suppliers/getById") {
            val suppliersController = SuppliersController(call)
            suppliersController.getSupplierById()
        }
    }
}