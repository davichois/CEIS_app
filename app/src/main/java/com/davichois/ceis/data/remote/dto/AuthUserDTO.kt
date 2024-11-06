package com.davichois.ceis.data.remote.dto

data class AuthUserDTO(
    val id: Int,
    val name: String,
    val dni: String,
    val nickname: String,
    val entity: String,
    val avatar: String?,
    val date: String,
    val isOpened: Boolean,
    val gender: String?,
    val code: String,
    val rol: String
)