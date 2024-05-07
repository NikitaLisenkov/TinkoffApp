package com.example.app.data.repository

import com.example.app.data.database.dao.StreamDao
import com.example.app.data.network.ZulipApi
import com.example.app.data.repository.mappers.domain.toDomain
import com.example.app.data.repository.mappers.entity.toEntity
import com.example.app.domain.model.StreamModel
import com.example.app.domain.repo.ChannelsRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelsRepositoryImpl @Inject constructor(
    private val api: ZulipApi,
    private val streamDao: StreamDao
) : ChannelsRepository {

    override suspend fun fetchStreamsWithTopics(onlySubscribed: Boolean) {
        val streams = if (onlySubscribed) {
            api.getStreamsSubscriptions()
        } else {
            api.getAllStreams()
        }.streams

        coroutineScope {
            streams.forEach { stream ->
                launch {
                    val topicsEntities = api.getTopics(stream.streamId).topics
                        .toEntity(streamId = stream.streamId)

                    val streamEntity = stream.toEntity(isSubscribed = onlySubscribed)

                    streamDao.insertStreamWithTopicList(
                        stream = streamEntity,
                        topicsList = topicsEntities
                    )
                }
            }
        }
    }

    override fun getAllStreamsWithTopicsFlow(onlySubscribed: Boolean): Flow<List<StreamModel>> {
        return if (onlySubscribed) {
            streamDao.getAllSubscribedStreamsWithTopics()
        } else {
            streamDao.getAllStreamsWithTopics()
        }.map { it.toDomain() }
    }

    override suspend fun updateStream(streamId: Long, isExpanded: Boolean) {
        streamDao.updateStream(streamId, isExpanded)
    }
}