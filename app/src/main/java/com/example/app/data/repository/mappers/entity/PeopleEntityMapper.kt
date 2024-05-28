package com.example.app.data.repository.mappers.entity

import com.example.app.data.database.entity.UserEntity
import com.example.app.data.network.model.UserResponse

fun UserResponse.toEntity(): UserEntity = UserEntity(
    userId = userId,
    deliveryEmail = deliveryEmail,
    email = email,
    fullName = fullName,
    avatarUrl = avatarUrl,
    isActive = isActive,
)