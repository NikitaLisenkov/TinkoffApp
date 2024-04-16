package com.example.app.data.model

import com.google.gson.annotations.SerializedName

data class GetTopicResponse(
    @SerializedName("topics")
    val topics: List<TopicResponse>
)

data class TopicResponse(
    @SerializedName("max_id")
    val maxId: Int,
    @SerializedName("name")
    val name: String,
)