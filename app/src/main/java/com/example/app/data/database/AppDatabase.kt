package com.example.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.app.data.database.dao.ChatDao
import com.example.app.data.database.dao.StreamDao
import com.example.app.data.database.dao.UserDao
import com.example.app.data.database.entity.MessageEntity
import com.example.app.data.database.entity.ReactionEntity
import com.example.app.data.database.entity.StreamEntity
import com.example.app.data.database.entity.TopicEntity
import com.example.app.data.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, StreamEntity::class, TopicEntity::class, MessageEntity::class, ReactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun streamDao(): StreamDao
    abstract fun chatDao(): ChatDao
}