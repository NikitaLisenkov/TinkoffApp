package com.example.app.data.model

import com.google.gson.annotations.SerializedName

data class GetMessageResponse(
    @SerializedName("messages")
    val messages: List<Message>
)

data class Message(
    @SerializedName("id")
    val msgId: Long,
    @SerializedName("sender_full_name")
    val senderName: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("sender_id")
    val senderId: Long,
    @SerializedName("timestamp")
    val time: Long,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("reactions")
    val reactions: List<ReactionResponse>,
    @SerializedName("subject")
    val subject: String
)

data class SendMessageResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("result")
    val result: ResultTypeResponse,
)

enum class ResultTypeResponse {
    @SerializedName("success")
    SUCCESS,

    @SerializedName("error")
    ERROR
}

data class ReactionResponse(
    @SerializedName("emoji_name")
    val emojiName: String,
    @SerializedName("emoji_code")
    val emojiCode: String,
    @SerializedName("reaction_type")
    val reactionType: String,
    @SerializedName("user_id")
    val userId: Long,
)
