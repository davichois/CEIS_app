package com.davichois.ceis.data.remote.dto

data class EventAttendanceDTO(
    val id: Long,
    val name: String,
    val date: String,
    val endDate: String,
    val place: String,
    val code: String,
    val modality: String,
    val typeEvent: String,
    val status: Boolean,
    val quota: Long
)
