package com.davichois.ceis.data.remote

import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.data.remote.dto.ResponseAPI
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthAPI {

    @POST("auth")
    suspend fun postAuthenticate(
        @Query("dni") dni: String,
        ): ResponseAPI<AuthUserDTO>

}