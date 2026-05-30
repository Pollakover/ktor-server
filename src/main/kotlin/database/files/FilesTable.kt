package com.example.database.files

import org.jetbrains.exposed.v1.core.Table

object FilesTable : Table("files") {

    val name = varchar("name", 25)
    val description = text("description")
    val filePath = text("file_path")
    val imagePath = text("image_path")
    val userLogin = varchar("userLogin", 25)
}