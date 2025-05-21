package com.example.features.warehouses

import com.example.database.warehouses.WarehouseDTO
import com.example.database.warehouses.Warehouses
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.UUID

class WarehousesController(private val call: ApplicationCall) {

    suspend fun fetchWarehouses() {
        try {
            val request = call.receive<FetchWarehousesRequest>()
            val warehouses = Warehouses.fetchWarehouses(request.user_login)
            call.respond(HttpStatusCode.OK, warehouses)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun addWarehouse() {
        val request = call.receive<AddWarehouseRequest>()
        val id = UUID.randomUUID().toString()
        try {
            Warehouses.insert(
                WarehouseDTO(
                    warehouseId = id,
                    name = request.name,
                    address = request.address,
                    postal_address = request.postal_address,
                    user_login = request.user_login
                )
            )
            call.respond(HttpStatusCode.OK, "Warehouse added")
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "Warehouse already exists!")
        }  catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }
}