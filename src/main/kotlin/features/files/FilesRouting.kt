package com.example.features.files

import com.example.database.files.FilesTable
import com.example.database.files.UploadResponse
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

fun Application.configureFilesRouting() {
    routing {

        staticFiles(
            remotePath = "/files",
            dir = File("uploads")
        )

        post("/upload") {

            val multipart = call.receiveMultipart()

            var fileName = ""
            var description = ""
            var userLogin = ""

            var savedFilePath = ""

            multipart.forEachPart { part ->

                when (part) {

                    is PartData.FormItem -> {

                        when (part.name) {

                            "description" -> {
                                description = part.value
                            }

                            "userLogin" -> {
                                userLogin = part.value
                            }

                            "name" -> {
                                fileName = part.value
                            }
                        }
                    }

                    is PartData.FileItem -> {

                        val originalFileName =
                            part.originalFileName ?: "unknown"

                        val extension =
                            File(originalFileName).extension

                        val generatedFileName =
                            "${System.currentTimeMillis()}.$extension"

                        val file =
                            File("uploads/$generatedFileName")

                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }

                        savedFilePath =
                            "http://192.168.1.6:8080/files/$generatedFileName"
                    }

                    else -> {}
                }

                part.dispose()
            }

            transaction {

                FilesTable.insert {

                    it[name] = fileName
                    it[FilesTable.description] = description
                    it[FilesTable.userLogin] = userLogin
                    it[filePath] = savedFilePath
                }
            }

            call.respond(
                UploadResponse(
                    success = true,
                    fileUrl = savedFilePath
                )
            )
        }
    }
}