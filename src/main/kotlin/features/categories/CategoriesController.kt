package com.example.features.categories

import com.example.database.categories.CategoryDTO
import com.example.database.categories.Categories
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException

class CategoriesController(private val call: ApplicationCall) {

    suspend fun fetchCategories() {
        try {
            val request = call.receive<FetchCategoriesRequest>()
            val categories = Categories.fetchCategories(request.model_id)
            call.respond(HttpStatusCode.OK, categories)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun addCategory() {
        val request = call.receive<AddCategoryRequest>()
        try {
            Categories.insert(
                CategoryDTO(
                    model_id = request.model_id,
                    name = request.name,
                )
            )
            call.respond(HttpStatusCode.OK, "Category added")
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Ошибка")))
            //call.respond(HttpStatusCode.Conflict, "Category already exists!")
        }  catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }
}