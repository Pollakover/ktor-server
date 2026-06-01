package com.example.database.models

import kotlinx.serialization.Serializable

@Serializable
class ModelDTO (
    val id: Int,
    val name: String,
    val description: String,
    val width: Double,
    val height: Double,
    val length: Double,
    val size: Double,
    val file_url: String,
    val image_url: String,
    val user_login: String,
)
@Serializable
data class DeleteModelResponse(
    val success: Boolean,
    val id: Int,
    val message: String? = null
)
@Serializable
data class UpdateModelResponse(
    val model: ModelDTO
)