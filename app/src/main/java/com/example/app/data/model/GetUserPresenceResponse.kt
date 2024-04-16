package com.example.app.data.model

import com.google.gson.annotations.SerializedName

data class GetUserPresenceResponse(
    @SerializedName("presence")
    val presence: Presence
)

data class Presence(
    @SerializedName("aggregated")
    val aggregated: Aggregated
)

data class Aggregated(
    @SerializedName("status")
    val status: PeopleStatusResponse,
    @SerializedName("timestamp")
    val time: Int
)

enum class PeopleStatusResponse {
    @SerializedName("active")
    ONLINE,

    @SerializedName("idle")
    IDLE,

    @SerializedName("offline")
    OFFLINE
}
