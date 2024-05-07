package com.example.app.data.repository.mappers.domain

import com.example.app.data.network.model.OwnProfileResponse
import com.example.app.domain.model.OwnProfileModel

fun OwnProfileResponse.toDomain(): OwnProfileModel = OwnProfileModel(
    usesId = this.userId,
    fullName = this.fullName,
    email = this.email,
    avatarUrl = this.avatarUrl,
    isActive = this.isActive
)


