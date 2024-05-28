package com.example.app.domain.repo

import com.example.app.domain.model.StreamModel
import kotlinx.coroutines.flow.Flow

interface ChannelsRepository {
    suspend fun fetchStreamsWithTopics(onlySubscribed: Boolean)
    fun getAllStreamsWithTopicsFlow(onlySubscribed: Boolean): Flow<List<StreamModel>>
    suspend fun updateStream(streamId: Long, isExpanded: Boolean)
}