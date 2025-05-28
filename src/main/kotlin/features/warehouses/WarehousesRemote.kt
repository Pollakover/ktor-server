package com.example.features.warehouses

import kotlinx.serialization.Serializable

@Serializable
data class FetchWarehousesRequest(
    val user_login: String
)

@Serializable
data class AddWarehouseRequest(
    val name: String,
    val address: String,
    val postal_address: String,
    val user_login: String
)

@Serializable
data class GetWarehouseByIdRequest(
    val warehouseId: String
)