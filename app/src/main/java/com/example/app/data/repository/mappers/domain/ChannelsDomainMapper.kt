package com.example.app.data.repository.mappers.domain

import com.example.app.data.database.entity.StreamWithTopicsEntity
import com.example.app.domain.model.StreamModel
import com.example.app.domain.model.TopicModel

fun List<StreamWithTopicsEntity>.toDomain() = this.map {
    StreamModel(
        streamId = it.streamEntity.streamId,
        name = it.streamEntity.streamName,
        isExpanded = it.streamEntity.isExpanded,
        topics = it.topicsList.map { topic ->
            TopicModel(
                maxId = topic.topicId,
                name = topic.topicName
            )
        }
    )
}