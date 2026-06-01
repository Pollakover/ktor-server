package com.example.database.categories

import ch.qos.logback.core.model.Model
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

object Categories : Table("categories") {
    val model_id = integer("model_id")
    val name = text("name")

    fun insert(categoryDTO: CategoryDTO) {
        transaction {
            Categories.insert {
                it[model_id] = categoryDTO.model_id
                it[name] = categoryDTO.name
            }
        }
    }

    fun fetchCategories(id: Int): List<CategoryDTO> {
        return try {
            transaction {
                Categories.selectAll().where { model_id eq id }.toList().map {
                    CategoryDTO(
                        model_id = it[model_id],
                        name = it[name],
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}