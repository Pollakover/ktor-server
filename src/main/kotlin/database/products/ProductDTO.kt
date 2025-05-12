package com.example.database.products
import kotlinx.serialization.Serializable

@Serializable
class ProductDTO(
    val productId: String,
    val name: String,
    val amount_sold: Int,
    val category: String,
    val price: Double,
    val user_login: String,
    val supplier: String,
    val warehouse: String,
    val image_data: String?,
)