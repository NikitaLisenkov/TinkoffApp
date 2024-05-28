package com.example.app.domain.repo

import com.example.app.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface PeopleRepository {
    suspend fun fetchAllUsers()
    fun getAllUsersFlow(): Flow<List<UserModel>>
}