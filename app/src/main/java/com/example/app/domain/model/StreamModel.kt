package com.example.app.domain.model

data class StreamModel(
    val streamId: Int,
    val name: String,
    val color: String = "#BCBCBC"
)