package com.example.app.domain.model

data class OwnProfileModel(
    val usesId: Int,
    val fullName: String,
    val email: String,
    val avatarUrl: String,
    val isActive: Boolean
)
