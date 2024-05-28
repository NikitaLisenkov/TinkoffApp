package com.example.app.presentation.people.model

data class PeopleUi(
    val id: Long,
    val avatarUrl: String?,
    val fullName: String,
    val email: String?,
    val isOnline: Boolean
)
