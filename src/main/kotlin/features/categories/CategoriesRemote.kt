package com.example.features.categories

import kotlinx.serialization.Serializable

@Serializable
data class FetchCategoriesRequest(
    val model_id: Int,
)

@Serializable
data class AddCategoryRequest(
    val model_id: Int,
    val name: String,
)