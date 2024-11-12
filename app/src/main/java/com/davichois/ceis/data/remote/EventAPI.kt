package com.davichois.ceis.data.remote

import com.davichois.ceis.data.remote.dto.AttendanceRegisterDTO
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.data.remote.dto.GenerateCodeDTO
import com.davichois.ceis.data.remote.dto.ResponseAPI
import retrofit2.http.Body
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

    @POST("event/stamp")
    suspend fun postAttendanceRecorder(
        @Query("token") token: String,
        @Query("dni") dni: String,
        @Query("code") code: String,
    ): ResponseAPI<AttendanceRegisterDTO>

    // Si vuelve a realizar peticion = 300 http
    @POST("event/bookings")
    suspend fun postBookingRecorder(
        @Query("token") token: String,
        @Query("dni") dni: String,
        @Body events: List<EventDTO>,
    ): ResponseAPI<String>

    // Evento por codigo
    @GET("event/filters")
    suspend fun getEventForCode(
        @Query("token") token: String,
        @Query("code") code: String
    ): ResponseAPI<EventDTO>

    // Evento por dia y type con filter
    @GET("event/filters")
    suspend fun getEventForDay(
        @Query("token") token: String,
        @Query("day") day: String,
        @Query("type") type: String
    ): ResponseAPI<List<EventDTO>>

    // Evento por dia con filter
    @GET("event/filters")
    suspend fun getEventForDayMaster(
        @Query("token") token: String,
        @Query("day") day: String,
    ): ResponseAPI<List<EventDTO>>

    // Si vuelve a realizar peticion = 300 http
    // NOT USE IMPLEMENT
    @POST("event/stamp")
    suspend fun postCodeSupportRecorder(
        @Query("token") token: String,
        @Query("codeAssistance") codeAssistance: String
    ): ResponseAPI<AttendanceRegisterDTO>

    // Todos los Evento
    // NOT USE IMPLEMENT
    @GET("event/filters")
    suspend fun getEventForAll(
        @Query("token") token: String
    ): ResponseAPI<Map<String, List<EventDTO>>>

}