package com.example.app.data.repository.mappers.entity

import com.example.app.data.database.entity.StreamEntity
import com.example.app.data.database.entity.TopicEntity
import com.example.app.data.network.model.StreamResponse
import com.example.app.data.network.model.TopicResponse

fun StreamResponse.toEntity(isSubscribed: Boolean, isExpanded: Boolean): StreamEntity = StreamEntity(
    streamId = this.streamId,
    streamName = this.name,
    isSubscribed = isSubscribed,
    isExpanded = isExpanded
)

fun List<TopicResponse>.toEntity(streamId: Long): List<TopicEntity> = this.map {
    TopicEntity(
        topicId = it.maxId,
        topicName = it.name,
        streamId = streamId
    )
}