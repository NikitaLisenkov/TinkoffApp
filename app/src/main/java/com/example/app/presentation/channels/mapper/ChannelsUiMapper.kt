package com.example.app.presentation.channels.mapper

import com.example.app.domain.model.StreamModel
import com.example.app.presentation.channels.model.ChannelsItemUi

fun List<StreamModel>.toUi(): List<ChannelsItemUi> = flatMap { stream ->
    val streams = listOf(
        ChannelsItemUi.StreamUi(
            id = stream.streamId,
            text = stream.name,
            isExpanded = stream.isExpanded,
        )
    )
    if (stream.isExpanded) {
        streams + stream.topics.map { topic ->
            ChannelsItemUi.TopicUi(
                id = topic.maxId,
                text = topic.name,
                streamName = stream.name,
                backgroundColorRes = 0
            )
        }
    } else {
        streams
    }
}