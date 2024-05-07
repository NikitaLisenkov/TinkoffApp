package com.example.app.data.network.model

import com.google.gson.annotations.SerializedName

data class OwnProfileResponse(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("is_active")
    val isActive: Boolean
)
