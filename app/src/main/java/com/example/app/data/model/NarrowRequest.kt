package com.example.app.data.model

import com.google.gson.annotations.SerializedName

data class NarrowRequest(
    @SerializedName("operator")
    val operator: String,

    @SerializedName("operand")
    val operand: String
)

enum class NarrowType(val stringValue: String) {
    STREAM_OPERATOR("stream"),
    TOPIC_OPERATOR("topic")
}