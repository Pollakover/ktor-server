package com.example.database.users

import kotlinx.serialization.Serializable

@Serializable
class UserDTO (
    val login: String,
    val password: String,
    val email: String?,
    val username: String
)