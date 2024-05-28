package com.example.app.domain.repo

import com.example.app.domain.model.OwnProfileModel

interface ProfileRepository {
suspend fun getOwnUserProfile() : OwnProfileModel
}