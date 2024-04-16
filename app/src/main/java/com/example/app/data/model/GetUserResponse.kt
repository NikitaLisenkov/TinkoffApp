package com.example.app.data.model

import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("user_id")
    val userId: Long,

    @SerializedName("delivery_email")
    val deliveryEmail: String?,
    val email: String?,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("avatar_url")
    val avatarUrl: String?,

    @SerializedName("is_active")
    val isActive: Boolean
)

