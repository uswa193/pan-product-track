package com.dicoding.picodiploma.pan.data



data class Message(
    val id: Int,
    val text: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val timestamp: Long? = null
)

enum class MessageType {
    USER,
    SYSTEM
}
