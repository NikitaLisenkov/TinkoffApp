package com.example.app.data.datasource

import com.example.app.domain.local.OwnUserLocalDataSource
import com.example.app.domain.model.OwnProfileModel
import javax.inject.Inject

class OwnUserLocalDataSourceImpl @Inject constructor() : OwnUserLocalDataSource {

    private var user: OwnProfileModel? = null

    override fun getUser(): OwnProfileModel? {
        return user
    }

    override fun saveUser(ownProfileModel: OwnProfileModel) {
        user = ownProfileModel
    }

    override fun clearUser() {
        user = null
    }
}