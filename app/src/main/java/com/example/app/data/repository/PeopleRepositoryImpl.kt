package com.example.app.data.repository

import com.example.app.data.network.ZulipApi
import com.example.app.data.repository.mappers.toDomain
import com.example.app.domain.model.UserModel
import com.example.app.domain.repo.PeopleRepository
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(private val api: ZulipApi) : PeopleRepository {

    override suspend fun getAllUsers(): List<UserModel> {
        return api.getAllUsers().toDomain()
    }

}