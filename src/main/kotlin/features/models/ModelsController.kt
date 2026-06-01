package com.example.features.models

import com.example.database.categories.Categories
import com.example.database.models.DeleteModelResponse
import com.example.database.models.Models
import com.example.database.models.UpdateModelResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.io.File

class ModelsController(private val call: ApplicationCall) {
    suspend fun fetchModels() {
        try {
            val models = Models.fetchModels()
            call.respond(HttpStatusCode.OK, models)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun deleteModel() {

        try {

            val request =
                call.receive<DeleteModelRequest>()

            val model =
                Models.getModelById(request.id)

            if (model == null) {

                call.respond(
                    HttpStatusCode.NotFound,
                    "Model not found"
                )

                return
            }

            model.file_url
                .substringAfter("/files/")
                .let {
                    File("uploads/$it").delete()
                }

            model.image_url
                .substringAfter("/files/")
                .let {
                    File("uploads/$it").delete()
                }

            Models.deleteModel(request.id)

            call.respond(DeleteModelResponse(success = true, id = request.id, message = "Deleted successfully"))

        } catch (e: Exception) {

            e.printStackTrace()

            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to (e.message ?: "Unknown error"))
            )
        }
    }

    suspend fun updateModel() {

        try {

            val request =
                call.receive<UpdateModelRequest>()

            val model =
                Models.getModelById(request.id)

            if (model == null) {

                call.respond(
                    HttpStatusCode.NotFound,
                    "Model not found"
                )

                return
            }

            Models.updateModel(
                id = request.id,
                name = request.name,
                description = request.description
            )

            transaction {

                Categories.deleteWhere {
                    Categories.model_id eq request.id
                }

                request.categories.forEach { category ->

                    Categories.insert {

                        it[model_id] = request.id
                        it[name] = category
                    }
                }
            }

            val updatedModel =
                Models.getModelById(request.id)
                    ?: error("Failed to load updated model")

            call.respond(
                UpdateModelResponse(
                    model = updatedModel
                )
            )

        } catch (e: Exception) {

            call.respond(
                HttpStatusCode.BadRequest,
                mapOf(
                    "error" to (e.message ?: "Unknown error")
                )
            )
        }
    }
}