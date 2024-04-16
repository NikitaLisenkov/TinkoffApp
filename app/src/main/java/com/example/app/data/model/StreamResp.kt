package com.example.app.data.model

import com.google.gson.annotations.SerializedName

data class GetStreamResponse(
    @SerializedName("subscriptions", alternate = ["streams"])
    val streams: List<StreamResponse>
)

data class StreamResponse(
    @SerializedName("stream_id")
    val streamId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String = "#BCBCBC"
)