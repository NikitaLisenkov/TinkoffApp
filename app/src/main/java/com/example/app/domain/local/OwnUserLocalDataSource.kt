package com.example.app.domain.local

import com.example.app.domain.model.OwnProfileModel

interface OwnUserLocalDataSource {

    fun getUser(): OwnProfileModel?

    fun saveUser(ownProfileModel: OwnProfileModel)

    fun clearUser()
}