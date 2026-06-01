package com.example.features.models

import com.example.database.models.Models
import com.example.features.orders.OrdersController
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.*
import io.ktor.server.http.content.staticFiles
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

fun Application.configureModelsRouting() {
    val uploadsDir = File("uploads")
    val modelsDir = File("uploads/models")
    val previewsDir = File("uploads/previews")

    if (!uploadsDir.exists()) uploadsDir.mkdirs()
    if (!modelsDir.exists()) modelsDir.mkdirs()
    if (!previewsDir.exists()) previewsDir.mkdirs()

    routing {
        staticFiles(
            remotePath = "/files",
            dir = File("uploads")
        )

        delete("/models/delete") {
            val modelsController = ModelsController(call)
            modelsController.deleteModel()
        }

        patch("/models/update") {
            val modelsController = ModelsController(call)
            modelsController.updateModel()
        }

        get("/models/fetch") {
            val modelsController = ModelsController(call)
            modelsController.fetchModels()
        }

        post("/upload") {
            val multipart = call.receiveMultipart()

            var fileName = ""
            var description = ""
            var width = Double.NaN
            var height = Double.NaN
            var length = Double.NaN
            var modelFilePath = ""
            var previewFilePath = ""
            var userLogin = ""
            val timestamp = System.currentTimeMillis()
            var sizeMb = Double.NaN

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "description" -> description = part.value
                            "user_login" -> userLogin = part.value
                            "name" -> fileName = part.value
                            "width" -> {
                                width = part.value.toDouble()
                            }
                            "height" -> {
                                height = part.value.toDouble()
                            }
                            "length" -> {
                                length = part.value.toDouble()
                            }
                        }
                    }

                    is PartData.FileItem -> {
                        val originalFileName = part.originalFileName ?: "unknown"
                        val extension = File(originalFileName).extension
                        val generatedFileName = "${timestamp}.$extension"

                        when {
                            part.name == "preview" -> {
                                // Сохраняем preview в папку previews
                                val file = File("uploads/previews/$generatedFileName")
                                part.streamProvider().use { input ->
                                    file.outputStream().buffered().use { output ->
                                        input.copyTo(output)
                                    }
                                }
                                previewFilePath = "http://192.168.1.6:8080/files/previews/$generatedFileName"
                            }
                            part.name == "file" -> {
                                // Сохраняем модель в папку models
                                val file = File("uploads/models/$generatedFileName")

                                // Сначала сохраняем файл
                                part.streamProvider().use { input ->
                                    file.outputStream().buffered().use { output ->
                                        input.copyTo(output)
                                    }
                                }
                                val sizeInBytes = file.length()
                                val sizeInMB = sizeInBytes / (1024.0 * 1024.0)
                                sizeMb = sizeInMB

                                modelFilePath = "http://192.168.1.6:8080/files/models/$generatedFileName"
                            }
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }

            val modelId = transaction {

                Models.insert {

                    it[name] = fileName
                    it[Models.description] = description
                    it[Models.user_login] = userLogin
                    it[Models.width] = width
                    it[Models.height] = height
                    it[Models.length] = length
                    it[Models.size] = sizeMb
                    it[image_url] = previewFilePath
                    it[file_url] = modelFilePath

                } get Models.id

            }

            call.respond(
                UploadResponse(
                    success = true,
                    fileUrl = modelFilePath,
                    previewUrl = previewFilePath,
                    id = modelId
                )
            )
        }
    }
}