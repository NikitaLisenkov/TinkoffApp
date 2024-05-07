package com.example.app.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.app.data.database.entity.StreamEntity
import com.example.app.data.database.entity.StreamWithTopicsEntity
import com.example.app.data.database.entity.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StreamDao {
    @Transaction
    suspend fun insertStreamWithTopicList(stream: StreamEntity, topicsList: List<TopicEntity>) {
        insertStream(stream)
        insertTopicsList(topicsList)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStream(stream: StreamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopicsList(topics: List<TopicEntity>)

    @Query("SELECT * FROM stream")
    @Transaction
    fun getAllStreamsWithTopics(): Flow<List<StreamWithTopicsEntity>>

    @Query("SELECT * FROM stream WHERE stream.is_subscribed = 1")
    @Transaction
    fun getAllSubscribedStreamsWithTopics(): Flow<List<StreamWithTopicsEntity>>

    @Query("UPDATE stream SET is_expanded = :isExpanded WHERE stream_id = :streamId")
    suspend fun updateStream(streamId: Long, isExpanded: Boolean)
}