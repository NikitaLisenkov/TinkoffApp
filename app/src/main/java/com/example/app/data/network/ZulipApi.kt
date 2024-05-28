package com.example.app.data.network

import com.example.app.data.network.model.EventRegisterQueueResponse
import com.example.app.data.network.model.EventResponse
import com.example.app.data.network.model.GetUserPresenceResponse
import com.example.app.data.network.model.MessagesResponse
import com.example.app.data.network.model.OwnProfileResponse
import com.example.app.data.network.model.ReactionResponse
import com.example.app.data.network.model.StreamsResponse
import com.example.app.data.network.model.TopicsResponse
import com.example.app.data.network.model.UserResponse
import com.example.app.data.network.model.UsersResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ZulipApi {

    @GET("users")
    suspend fun getAllUsers(): UsersResponse

    @GET("users/{user_id}")
    suspend fun getUser(
        @Path("user_id") userId: Int
    ): UserResponse

    @GET("users/me")
    suspend fun getOwnUserProfile(): OwnProfileResponse

    @GET("users/{user_id_or_email}/presence")
    suspend fun getUserPresence(
        @Path("user_id_or_email") id: Int
    ): GetUserPresenceResponse

    @GET("messages")
    suspend fun getMessages(
        @Query("anchor") anchor: String = "newest",
        @Query("num_before") numBefore: Int = 5000,
        @Query("num_after") numAfter: Int = 0,
        @Query("narrow") narrow: String,
        @Query("apply_markdown") applyMarkdown: Boolean = false
    ): MessagesResponse

    @POST("messages")
    suspend fun sendMessage(
        @Query("type") type: String = "stream",
        @Query("to") streamName: String,
        @Query("content") content: String,
        @Query("topic") topicName: String,
    )

    @DELETE("messages/{message_id}/reactions")
    suspend fun deleteEmoji(
        @Path("message_id") messageId: Long,
        @Query("emoji_name") emojiName: String
    ): ReactionResponse

    @POST("messages/{message_id}/reactions")
    suspend fun addEmoji(
        @Path("message_id") messageId: Long,
        @Query("emoji_name") emojiName: String
    ): ReactionResponse

    @GET("streams")
    suspend fun getAllStreams(): StreamsResponse

    @GET("users/me/subscriptions")
    suspend fun getStreamsSubscriptions(): StreamsResponse

    @GET("users/me/{stream_id}/topics")
    suspend fun getTopics(
        @Path("stream_id") streamId: Long
    ): TopicsResponse

    @POST("register")
    suspend fun registerEvent(
        @Query("event_types") eventType: String = """["message", "reaction"]""",
    ): EventRegisterQueueResponse

    @GET("events")
    suspend fun getEventsFromQueue(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: Long = -1
    ): EventResponse
}