package com.davichois.ceis.data.remote.dto

data class AttendanceRegisterDTO (
    val id: Long,
    val date: String,
    val user: UserDTO,
    val event: EventAttendanceDTO
)