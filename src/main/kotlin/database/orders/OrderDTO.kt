package com.example.database.orders
import kotlinx.serialization.Serializable

@Serializable
class OrderDTO (
    val orderId: String,
    val amount: Int,
    val delivery_date: String,
    val user_login: String,
    val status: String,
    val number: Int,
    val product: String,
    val price: Double
)