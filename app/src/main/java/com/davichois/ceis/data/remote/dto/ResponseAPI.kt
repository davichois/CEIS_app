package com.davichois.ceis.data.remote.dto

data class ResponseAPI<T>(
    val data: T,
    val origin: String,
    val status: String,
    val url: String
)