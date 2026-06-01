package com.example.features.categories

import com.example.features.categories.CategoriesController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCategoriesRouting() {
    routing {
        post("/categories/fetch") {
            val categoriesController = CategoriesController(call)
            categoriesController.fetchCategories()
        }

        post("/categories/add") {
            val categoriesController = CategoriesController(call)
            categoriesController.addCategory()
        }
    }
}