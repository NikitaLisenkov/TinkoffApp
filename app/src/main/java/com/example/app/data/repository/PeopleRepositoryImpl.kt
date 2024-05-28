package com.example.app.data.repository

import com.example.app.data.database.dao.UserDao
import com.example.app.data.network.ZulipApi
import com.example.app.data.repository.mappers.domain.toDomain
import com.example.app.data.repository.mappers.entity.toEntity
import com.example.app.domain.model.UserModel
import com.example.app.domain.repo.PeopleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val api: ZulipApi,
    private val userDao: UserDao
) : PeopleRepository {

    override suspend fun fetchAllUsers() {
        api.getAllUsers().members.map { it.toEntity() }.apply {
            userDao.insertUsers(this)
        }
    }

    override fun getAllUsersFlow(): Flow<List<UserModel>> {
        return userDao.getAllUsersFlow().map { it.toDomain() }
    }
}