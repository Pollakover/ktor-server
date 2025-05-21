package com.example.database.warehouses

import kotlinx.serialization.Serializable

@Serializable
class WarehouseDTO (
    val warehouseId: String,
    val name: String,
    val address: String,
    val postal_address: String,
    val user_login: String
)