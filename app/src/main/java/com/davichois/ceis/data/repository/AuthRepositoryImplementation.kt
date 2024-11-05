package com.davichois.ceis.data.repository

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.AuthAPI
import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImplementation(
    private val api: AuthAPI
): AuthRepository {

    override suspend fun postAuthenticate(dni: String): Resource<AuthUserDTO> {
        return try {
            val result =
                api.postAuthenticate(dni = dni).data
            Resource.Success(data = result)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                val result =
                    api.postAuthenticate(dni = dni).data
                Resource.Success(data = result)
            } else {
                Resource.Error(message = e.localizedMessage ?: "An unexpected error")
            }
        } catch (e: IOException) {
            Resource.Error(message = "Couldn't reach server")
        }
    }

}