package com.example.database.models

import kotlinx.serialization.Serializable

@Serializable
class ModelDTO (
    val modelId: Int,
    val amount: Int,
    val delivery_date: String,
    val user_login: String,
    val status: String,
    val number: Int,
    val product: String,
    val price: Double
)