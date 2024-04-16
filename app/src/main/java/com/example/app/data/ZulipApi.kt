package com.example.app.data

import com.example.app.data.model.EventRegisterQueueResponse
import com.example.app.data.model.EventResponse
import com.example.app.data.model.GetAllUserResponse
import com.example.app.data.model.GetMessageResponse
import com.example.app.data.model.GetOwnUserProfileResponse
import com.example.app.data.model.GetStreamResponse
import com.example.app.data.model.GetTopicResponse
import com.example.app.data.model.GetUserPresenceResponse
import com.example.app.data.model.GetUserResponse
import com.example.app.data.model.ReactionResponse
import com.example.app.data.model.SendMessageResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ZulipApi {

    @GET("users")
    suspend fun getAllUsers(): GetAllUserResponse

    @GET("users/{user_id}")
    suspend fun getUser(
        @Path("user_id") userId: Int
    ): GetUserResponse

    @GET("users/me")
    suspend fun getOwnUserProfile(): GetOwnUserProfileResponse

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
    ): GetMessageResponse

    @POST("messages")
    suspend fun sendMessage(
        @Query("type") type: String = "stream",
        @Query("to") streamName: String,
        @Query("content") content: String,
        @Query("topic") topicName: String,
    ): SendMessageResponse

    @DELETE("messages/{message_id}/reactions")
    suspend fun deleteEmoji(
        @Path("message_id") messageId: Long,
        @Query("emoji_name") emojiName: String
    ): ReactionResponse

    @POST("messages/{message_id}/reactions")
    suspend fun addEmoji(
        @Path("message_id") messageId: Long,
        @Query("emoji_code") emojiCode: String,
        @Query("emoji_name") emojiName: String
    ): ReactionResponse

    @GET("streams")
    suspend fun getAllStreams(): GetStreamResponse

    @GET("users/me/subscriptions")
    suspend fun getStreamsSubscriptions(): GetStreamResponse

    @GET("users/me/{stream_id}/topics")
    suspend fun getTopics(
        @Path("stream_id") streamId: Int
    ): GetTopicResponse

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