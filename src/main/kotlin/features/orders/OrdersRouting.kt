package com.example.features.orders

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureOrdersRouting() {
    routing {
        post("/orders/fetch") {
            val ordersController = OrdersController(call)
            ordersController.fetchOrders()
        }

        post("/orders/add") {
            val ordersController = OrdersController(call)
            ordersController.addOrder()
        }
    }
}