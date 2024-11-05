package com.davichois.ceis.domain.repository

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AttendanceRegisterDTO
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.data.remote.dto.GenerateCodeDTO

interface EventRepository {

    suspend fun getGenerateCodeAssistance(
        token: String,
        dni: String,
        code: String,
    ): Resource<GenerateCodeDTO>

    suspend fun postAttendanceRecorder(
        token: String,
        dni: String,
        code: String,
    ): Resource<AttendanceRegisterDTO>

    suspend fun postCodeSupportRecorder(
        token: String,
        codeAssistance: String
    ): Resource<AttendanceRegisterDTO>

    suspend fun getEventForCode(
        token: String,
        code: String
    ): Resource<EventDTO>

    suspend fun getEventForDay(
        token: String,
        day: String
    ): Resource<List<EventDTO>>

    suspend fun getEventForAll(
        token: String
    ): Resource<Map<String, List<EventDTO>>>

}