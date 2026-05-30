package com.example.database.files

data class UploadResponse(
    val success: Boolean,
    val fileUrl: String,
    val previewUrl: String? = null  // Добавлено поле для URL preview
)