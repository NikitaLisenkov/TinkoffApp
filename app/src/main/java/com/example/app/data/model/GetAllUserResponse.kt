package com.example.app.data.model

import com.google.gson.annotations.SerializedName

data class GetAllUserResponse(
    @SerializedName("members")
    val members: List<GetUserResponse>
)

