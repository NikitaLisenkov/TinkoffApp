package com.example.app.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.app.data.database.entity.MessageEntity
import com.example.app.data.database.entity.MessagesWithReactions
import com.example.app.data.database.entity.ReactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query(
        "DELETE FROM message WHERE message.stream_name = :streamName and message.topic_name = :topicName"
    )
    suspend fun clearMessages(
        streamName: String,
        topicName: String
    )

    @Query(
        "SELECT * FROM message WHERE message.stream_name = :streamName AND " +
                "message.topic_name = :topicName ORDER BY message.time DESC"
    )
    suspend fun getMessages(streamName: String, topicName: String): List<MessageEntity>

    @Query("DELETE FROM message WHERE message_id IN (:messagesIds)")
    suspend fun deleteMessages(messagesIds: List<Long>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReactions(reactionsList: List<ReactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReaction(reaction: ReactionEntity)

    @Delete
    suspend fun deleteReaction(reaction: ReactionEntity)

    @Query(
        "SELECT * FROM message WHERE message.stream_name = :streamName AND " +
                "message.topic_name = :topicName ORDER BY message.message_id"
    )
    @Transaction
    fun getAllMessagesWithReactionsFlow(
        streamName: String,
        topicName: String
    ): Flow<List<MessagesWithReactions>>

    @Query("SELECT * FROM message WHERE message_id = :messageId LIMIT 1")
    @Transaction
    suspend fun getMessageWithReaction(messageId: Long): MessagesWithReactions?

    @Query("SELECT COUNT(*) FROM message WHERE stream_name = :streamName AND topic_name = :topicName")
    suspend fun getMessagesCount(
        streamName: String,
        topicName: String
    ): Int

    @Query("SELECT MIN(message_id) FROM message WHERE stream_name = :streamName AND topic_name = :topicName")
    suspend fun getFirstMessageId(streamName: String, topicName: String): Long?

}