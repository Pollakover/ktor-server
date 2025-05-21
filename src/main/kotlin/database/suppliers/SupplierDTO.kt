package com.example.database.suppliers

import kotlinx.serialization.Serializable

@Serializable
class SupplierDTO(
    val supplierId: String,
    val name: String,
    val phone_number: String,
    val user_login: String,
    val type: String,
    val products: List<String> = emptyList()
)