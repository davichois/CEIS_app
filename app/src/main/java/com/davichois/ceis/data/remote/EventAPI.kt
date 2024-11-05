package com.davichois.ceis.data.remote

import com.davichois.ceis.data.remote.dto.AttendanceRegisterDTO
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.data.remote.dto.GenerateCodeDTO
import com.davichois.ceis.data.remote.dto.ResponseAPI
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EventAPI {

    @GET("event/gen-code-assistance")
    suspend fun getGenerateCodeAssistance(
        @Query("token") token: String,
        @Query("dni") dni: String,
        @Query("code") code: String,
    ): ResponseAPI<GenerateCodeDTO>

    // Si vuelve a realizar peticion = 300 http
    @POST("event/assistance")
    suspend fun postAttendanceRecorder(
        @Query("token") token: String,
        @Query("dni") dni: String,
        @Query("code") code: String,
    ): ResponseAPI<AttendanceRegisterDTO>

    // Si vuelve a realizar peticion = 300 http
    @POST("event/assistance")
    suspend fun postCodeSupportRecorder(
        @Query("token") token: String,
        @Query("codeAssistance") codeAssistance: String
    ): ResponseAPI<AttendanceRegisterDTO>

    // Evento por codigo
    // TODO: IMPLEMENT
    @GET("event/filters")
    suspend fun getEventForCode(
        @Query("token") token: String,
        @Query("code") code: String
    ): ResponseAPI<EventDTO>

    // Evento por dia
    // TODO: IMPLEMENT
    @GET("event/filters")
    suspend fun getEventForDay(
        @Query("token") token: String,
        @Query("day") day: String
    ): ResponseAPI<List<EventDTO>>

    // Todos los Evento
    // TODO: IMPLEMENT
    @GET("event/filters")
    suspend fun getEventForAll(
        @Query("token") token: String
    ): ResponseAPI<Map<String, List<EventDTO>>>

}