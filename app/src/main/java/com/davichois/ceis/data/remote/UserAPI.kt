package com.davichois.ceis.data.remote

import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.data.remote.dto.ResponseAPI
import com.davichois.ceis.data.remote.dto.UserCompleteDTO
import com.davichois.ceis.data.remote.dto.UserDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAPI {

    @PUT("user/updateInfo")
    suspend fun putUserInfo(
        @Query("token") token: String,
        @Query("dni") dni: String,
        @Body userCompleteDTO: UserCompleteDTO,
    ): ResponseAPI<AuthUserDTO>

    @GET("user/{dni}/get-records")
    suspend fun getRecordsUser(
        @Path("dni") dni: String,
        @Query("day") day: String,
    ): ResponseAPI<List<EventDTO>>

    @GET("user/filters")
    suspend fun getUserForDNI(
        @Query("token") token: String,
        @Query("dni") dni: String
    ): ResponseAPI<UserDTO>

}