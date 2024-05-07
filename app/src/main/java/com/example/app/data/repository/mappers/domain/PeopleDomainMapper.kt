package com.example.app.data.repository.mappers.domain

import com.example.app.data.database.entity.UserEntity
import com.example.app.data.network.model.UsersResponse
import com.example.app.domain.model.UserModel

fun UsersResponse.toDomain(): List<UserModel> = this.members.map {
    UserModel(
        userId = it.userId,
        deliveryEmail = it.deliveryEmail,
        email = it.email,
        fullName = it.fullName,
        avatarUrl = it.avatarUrl,
        isActive = it.isActive
    )
}

fun List<UserEntity>.toDomain(): List<UserModel> = this.map {
    UserModel(
        userId = it.userId,
        deliveryEmail = it.deliveryEmail,
        email = it.email,
        fullName = it.fullName,
        avatarUrl = it.avatarUrl,
        isActive = it.isActive
    )
}