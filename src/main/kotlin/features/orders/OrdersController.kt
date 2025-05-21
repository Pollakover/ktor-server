package com.example.features.orders

import com.example.database.orders.OrderDTO
import com.example.database.orders.Orders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.UUID

class OrdersController(private val call: ApplicationCall) {

    suspend fun fetchOrders() {
        try {
            val request = call.receive<FetchOrdersRequest>()
            val orders = Orders.fetchOrders(request.user_login)
            call.respond(HttpStatusCode.OK, orders)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun addOrder() {
        val request = call.receive<AddOrderRequest>()
        val id = UUID.randomUUID().toString()
        try {
            Orders.insert(
                OrderDTO(
                    orderId = id,
                    amount = request.amount,
                    delivery_date = request.delivery_date,
                    user_login = request.user_login,
                    status = request.status,
                    product = request.product,
                    number = 0
                )
            )
            call.respond(HttpStatusCode.OK, "Order added")
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Ошибка")))
            //call.respond(HttpStatusCode.Conflict, "Order already exists!")
        }  catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }
}