package com.example.app.data.network.model

import com.google.gson.annotations.SerializedName

data class StreamsResponse(
    @SerializedName("subscriptions", alternate = ["streams"])
    val streams: List<StreamResponse>
)

data class StreamResponse(
    @SerializedName("stream_id")
    val streamId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String? = "#BCBCBC"
)