package com.example.features.products

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureProductsRouting() {
    routing {
        post("/products/fetch") {
            val productsController = ProductsController(call)
            productsController.fetchProducts()
        }

        post("/products/add") {
            val productsController = ProductsController(call)
            productsController.addProduct()
        }
    }
}