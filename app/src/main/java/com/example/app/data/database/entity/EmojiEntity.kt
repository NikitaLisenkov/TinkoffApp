package com.example.app.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emojis")
data class EmojiEntity(
    @PrimaryKey
    @ColumnInfo("emoji_id")
    val id: Long,
    @ColumnInfo("emoji_name")
    val emojiName: String,
    @ColumnInfo("emoji_code")
    val emojiCode: Int
)
