package com.example.features.products

import kotlinx.serialization.Serializable

@Serializable
data class FetchProductsRequest(
    val user_login: String
)

@Serializable
data class AddProductRequest(
    val name: String,
    val amount_sold: Int,
    val category: String,
    val price: Double,
    val user_login: String,
    val supplier: String,
    val warehouse: String,
    val image_data: String?,
    val amount: Int,
)