package com.example.app.data.model

import com.google.gson.annotations.SerializedName

data class GetOwnUserProfileResponse(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("is_active")
    val isActive: Boolean
)
