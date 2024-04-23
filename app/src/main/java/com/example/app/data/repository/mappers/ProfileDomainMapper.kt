package com.example.app.data.repository.mappers

import com.example.app.data.network.model.OwnProfileResponse
import com.example.app.domain.model.OwnProfileModel

fun OwnProfileResponse.toDomain(): OwnProfileModel = OwnProfileModel(
    fullName = this.fullName,
    email = this.email,
    avatarUrl = this.avatarUrl,
    isActive = this.isActive
)


