package com.example.app.domain.model

data class StreamModel(
    val streamId: Long,
    val name: String,
    val color: String = "#BCBCBC",
    val isExpanded: Boolean,
    val topics: List<TopicModel>
)