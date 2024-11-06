package com.davichois.ceis.data.remote.dto

data class UserDTO(
    val id: Long,
    val name: String,
    val dni: String,
    val nickname: String,
    val entity: String,
    val avatar: String? = null,
    val date: String,
    val isOpened: Boolean,
    val gender: String? = null,
    val code: String,
    val rol: String
)
