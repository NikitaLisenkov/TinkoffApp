package com.example.app.domain.model

data class UserModel(
    val userId: Long,
    val deliveryEmail: String?,
    val email: String?,
    val fullName: String,
    val avatarUrl: String?,
    val isActive: Boolean
)