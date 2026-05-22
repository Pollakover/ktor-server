package com.example.database.files

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object FilesTable : Table("files") {

    val name = varchar("name", 25)
    val description = text("description")
    val filePath = text("file_path")
    val userLogin = varchar("userLogin", 25)
}