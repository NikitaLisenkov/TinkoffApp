package com.example.app.domain.repo

import com.example.app.domain.model.UserModel

interface PeopleRepository {
    suspend fun getAllUsers(): List<UserModel>
}