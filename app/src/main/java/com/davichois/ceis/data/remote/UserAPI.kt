package com.davichois.ceis.data.remote

import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.data.remote.dto.ResponseAPI
import com.davichois.ceis.data.remote.dto.UserCompleteDTO
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserAPI {

    @PUT("user/updateInfo")
    suspend fun putUserInfo(
        @Query("token") token: String,
        @Query("dni") dni: String,
        @Body userCompleteDTO: UserCompleteDTO,
    ): ResponseAPI<AuthUserDTO>

}