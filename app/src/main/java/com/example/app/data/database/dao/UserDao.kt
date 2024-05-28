package com.example.app.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app.data.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(usersList: List<UserEntity>)

    @Query("SELECT * FROM user")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user where user.user_id = :userId")
    suspend fun getUserById(userId: Long): UserEntity
}