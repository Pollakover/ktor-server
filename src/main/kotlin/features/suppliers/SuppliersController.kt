package com.example.features.suppliers

import com.example.database.suppliers.SupplierDTO
import com.example.database.suppliers.Suppliers
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import java.util.UUID

class SuppliersController(private val call: ApplicationCall) {

    suspend fun fetchSuppliers() {
        try {
            val request = call.receive<FetchSuppliersRequest>()
            val suppliers = Suppliers.fetchSuppliers(request.user_login)
            call.respond(HttpStatusCode.OK, suppliers)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun addSupplier() {
        val request = call.receive<AddSupplierRequest>()
        val id = UUID.randomUUID().toString()
        try {
            Suppliers.insert(
                SupplierDTO(
                    supplierId = id,
                    name = request.name,
                    phone_number = request.phone_number,
                    type = request.type,
                    user_login = request.user_login,
                )
            )
            call.respond(HttpStatusCode.OK, "Supplier added")
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "Supplier already exists!")
        }  catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun getSupplierById() {
        try {
            val request = call.receive<GetSupplierByIdRequest>()
            val supplier = Suppliers.getSupplierById(request.supplierId)
            if (supplier != null) {
                call.respond(HttpStatusCode.OK, supplier)
            } else {
                call.respond(HttpStatusCode.NotFound, "Supplier not found")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }
}

