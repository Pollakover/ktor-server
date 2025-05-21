package com.example.features.orders

import kotlinx.serialization.Serializable

@Serializable
data class FetchOrdersRequest(
    val user_login: String
)

@Serializable
data class AddOrderRequest(
    val amount: Int,
    val delivery_date: String,
    val user_login: String,
    val status: String,
    val product: String,
)