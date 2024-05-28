package com.example.app.data.network.model

import com.google.gson.annotations.SerializedName

data class MessagesResponse(
    @SerializedName("messages")
    val messages: List<MessageResponse>
)

data class MessageResponse(
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
)

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