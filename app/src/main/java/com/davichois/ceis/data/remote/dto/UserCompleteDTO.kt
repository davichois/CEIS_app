package com.davichois.ceis.data.remote.dto

data class UserCompleteDTO (
    val name: String,
    val nickname: String,
    val entity: String,
    val avatar: String? = null,
    val date: String,
    val isOpened: Boolean,
    val gender: String
)