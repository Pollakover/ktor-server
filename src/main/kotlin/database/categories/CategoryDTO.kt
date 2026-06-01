package com.example.database.categories

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO (
    val model_id: Int,
    val name: String,
)
