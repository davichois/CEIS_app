package com.davichois.ceis.data.remote.dto

data class EventDTO (
    val id: Long,
    val name: String,
    val date: String,
    val place: String,
    val code: String,
    val modality: String,
    val typeEvent: String,
    val status: Boolean,
    val quota: Long,
    val speakers: List<SpeakerEventDTO>,
    val father: Any? = null
)
