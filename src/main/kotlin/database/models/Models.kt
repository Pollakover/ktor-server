package com.example.database.models

import com.example.database.categories.Categories
import com.example.database.orders.OrderDTO
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

object Models : Table("models") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 25)
    val description = text("description")
    val width = double("width")
    val height = double("height")
    val length = double("length")
    val size = double("size")
    val file_url = text("file_url")
    val image_url = text("image_url")
    val user_login = varchar("user_login", 25)

    override val primaryKey = PrimaryKey(Models.id)

    fun fetchModels(): List<ModelDTO> {
        return try {
            transaction {
                Models.selectAll().map { row ->
                    ModelDTO(
                        id = row[Models.id],
                        name = row[Models.name],
                        description = row[Models.description],
                        width = row[Models.width],
                        height = row[Models.height],
                        length = row[Models.length],
                        size = row[Models.size],
                        file_url = row[Models.file_url],
                        image_url = row[Models.image_url],
                        user_login = row[Models.user_login],
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getModelById(modelId: Int): ModelDTO? {
        return transaction {
            Models
                .selectAll()
                .where { Models.id eq modelId }
                .singleOrNull()
                ?.let {
                    ModelDTO(
                        id = it[Models.id],
                        name = it[name],
                        description = it[description],
                        width = it[width],
                        height = it[height],
                        length = it[length],
                        size = it[size],
                        file_url = it[file_url],
                        image_url = it[image_url],
                        user_login = it[user_login]
                    )
                }
        }
    }

    fun deleteModel(modelId: Int): Int {
        return transaction {

            Categories.deleteWhere {
                Categories.model_id eq modelId
            }

            Models.deleteWhere {
                Models.id eq modelId
            }
        }
    }

    fun updateModel(
        id: Int,
        name: String,
        description: String
    ) {
        transaction {

            Models.update(
                where = { Models.id eq id }
            ) {
                it[Models.name] = name
                it[Models.description] = description
            }
        }
    }
}