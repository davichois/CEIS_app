package com.davichois.ceis.domain.repository

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.data.remote.dto.UserCompleteDTO

interface UserRepository {

    suspend fun putUserInfo(
        token: String,
        dni: String,
        userCompleteDTO: UserCompleteDTO,
    ): Resource<AuthUserDTO>

}