package me.gincos.rhoexercise.network.responses

data class Status(
    val created_at: String,
    val id_str: String,
    val text: String,
    val user: User?
)

data class User(
    val description: String?,
    val id: Long,
    val name: String,
    val screen_name: String?
)