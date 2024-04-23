package com.example.app.data.repository.mappers

import com.example.app.data.network.model.StreamsResponse
import com.example.app.data.network.model.TopicsResponse
import com.example.app.domain.model.StreamModel
import com.example.app.domain.model.TopicModel

fun StreamsResponse.toDomain(): List<StreamModel> = this.streams.map {
    StreamModel(
        streamId = it.streamId,
        name = it.name,
        color = it.color ?: "#BCBCBC"
    )
}

fun TopicsResponse.toDomain(): List<TopicModel> = this.topics.map {
    TopicModel(
        maxId = it.maxId,
        name = it.name
    )
}