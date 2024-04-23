package com.example.app.domain.repo

import com.example.app.domain.model.StreamModel
import com.example.app.domain.model.TopicModel

interface ChannelsRepository {
    suspend fun getStreamsSubscriptions(): List<StreamModel>
    suspend fun getAllStreams(): List<StreamModel>
    suspend fun getTopics(streamId: Int): List<TopicModel>
}