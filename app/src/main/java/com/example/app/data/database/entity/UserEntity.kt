package com.example.app.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo("user_id")
    val userId: Long,
    @ColumnInfo("delivery_email")
    val deliveryEmail: String?,
    @ColumnInfo("email")
    val email: String?,
    @ColumnInfo("full_name")
    val fullName: String,
    @ColumnInfo("avatar_url")
    val avatarUrl: String?,
    @ColumnInfo("is_active")
    val isActive: Boolean
)
