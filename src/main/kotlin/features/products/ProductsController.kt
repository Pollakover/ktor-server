package com.example.features.products

import com.example.database.products.ProductDTO
import com.example.database.products.Products
import com.example.database.users.UserDTO
import com.example.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*

class ProductsController(private val call: ApplicationCall) {
    suspend fun fetchProducts() {
        try {
            val request = call.receive<FetchProductsRequest>()
            val products = Products.fetchProducts(request.user_login)
            call.respond(HttpStatusCode.OK, products)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun addProduct() {
        val request = call.receive<AddProductRequest>()
        val id = UUID.randomUUID().toString()
        try {
            Products.insert(
                ProductDTO(
                    productId = id,
                    name = request.name,
                    amount_sold = request.amount_sold,
                    category = request.category,
                    price = request.price,
                    user_login = request.user_login,
                    supplier = request.supplier,
                    warehouse = request.warehouse,
                    image_data = request.image_data,
                )
            )
            call.respond(HttpStatusCode.OK, "Product added")
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "Product already exists!")
        }  catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }
}

