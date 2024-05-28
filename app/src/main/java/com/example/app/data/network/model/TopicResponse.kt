package com.example.app.data.network.model

import com.google.gson.annotations.SerializedName

data class TopicsResponse(
    @SerializedName("topics")
    val topics: List<TopicResponse>
)

data class TopicResponse(
    @SerializedName("max_id")
    val maxId: Long,
    @SerializedName("name")
    val name: String,
)