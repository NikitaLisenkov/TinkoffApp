package com.example.app.data.repository

import com.example.app.data.network.ZulipApi
import com.example.app.data.repository.mappers.domain.toDomain
import com.example.app.domain.local.OwnUserLocalDataSource
import com.example.app.domain.model.OwnProfileModel
import com.example.app.domain.repo.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ZulipApi,
    private val ownUserLocalDataSource: OwnUserLocalDataSource
) : ProfileRepository {

    override suspend fun getOwnUserProfile(): OwnProfileModel {
        val localUser = ownUserLocalDataSource.getUser()
        return if (localUser == null) {
            val response = api.getOwnUserProfile().toDomain()
            ownUserLocalDataSource.saveUser(response)
            response
        } else {
            localUser
        }
    }

}