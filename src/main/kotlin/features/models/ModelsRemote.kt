package com.example.features.models

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val success: Boolean,
    val fileUrl: String,
    val previewUrl: String? = null,
    val id: Int
)
@Serializable
data class DeleteModelRequest(
    val id: Int
)
@Serializable
data class UpdateModelRequest(
    val id: Int,
    val name: String,
    val description: String,
    val categories: List<String>
)