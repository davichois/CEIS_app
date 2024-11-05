package com.davichois.ceis.domain.repository

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AuthUserDTO

interface AuthRepository {

    suspend fun postAuthenticate(dni: String): Resource<AuthUserDTO>

}