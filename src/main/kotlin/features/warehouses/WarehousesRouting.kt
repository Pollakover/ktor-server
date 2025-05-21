package com.example.features.warehouses

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureWarehousesRouting() {
    routing {
        post("/warehouses/fetch") {
            val warehousesController = WarehousesController(call)
            warehousesController.fetchWarehouses()
        }

        post("/warehouses/add") {
            val warehousesController = WarehousesController(call)
            warehousesController.addWarehouse()
        }
    }
}