package com.davichois.ceis.data.remote.dto

data class SpeakerEventDTO (
    val id: Long,
    val name: String? = null,
    val degree: String? = null,
    val function: String? = null,
    val profile: String? = null,
    val image: String? = null
)
