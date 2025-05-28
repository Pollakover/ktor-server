package com.example.features.suppliers

import kotlinx.serialization.Serializable

@Serializable
data class FetchSuppliersRequest(
    val user_login: String
)

@Serializable
data class AddSupplierRequest(
    val name: String,
    val phone_number: String,
    val type: String,
    val user_login: String,
)

@Serializable
data class GetSupplierByIdRequest(
    val supplierId: String
)