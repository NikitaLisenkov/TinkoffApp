package com.example.app.presentation.people.model

data class People(
    val id: Long,
    val avatarUrl: String?,
    val fullName: String,
    val email: String?,
    val isOnline: Boolean
)

enum class PeopleStatus {
    ONLINE,
    OFFLINE
}