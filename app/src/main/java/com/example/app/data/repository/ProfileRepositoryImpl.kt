package com.example.app.data.repository

import com.example.app.data.network.ZulipApi
import com.example.app.data.repository.mappers.toDomain
import com.example.app.domain.model.OwnProfileModel
import com.example.app.domain.repo.ProfileRepository

class ProfileRepositoryImpl(private val api: ZulipApi) : ProfileRepository {

    override suspend fun getOwnUserProfile(): OwnProfileModel {
        val response = api.getOwnUserProfile()
        return response.toDomain()
    }

}