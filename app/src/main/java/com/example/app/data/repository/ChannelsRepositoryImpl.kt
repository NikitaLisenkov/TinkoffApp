package com.example.app.data.repository

import com.example.app.data.network.ZulipApi
import com.example.app.data.repository.mappers.toDomain
import com.example.app.domain.model.StreamModel
import com.example.app.domain.model.TopicModel
import com.example.app.domain.repo.ChannelsRepository
import javax.inject.Inject

class ChannelsRepositoryImpl @Inject constructor(private val api: ZulipApi) : ChannelsRepository {

    override suspend fun getStreamsSubscriptions(): List<StreamModel> {
        return api.getStreamsSubscriptions().toDomain()
    }

    override suspend fun getAllStreams(): List<StreamModel> {
        return api.getAllStreams().toDomain()
    }

    override suspend fun getTopics(streamId: Int): List<TopicModel> {
        return api.getTopics(streamId).toDomain()
    }
}