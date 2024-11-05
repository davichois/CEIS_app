package com.davichois.ceis.data.repository

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.UserAPI
import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.data.remote.dto.UserCompleteDTO
import com.davichois.ceis.domain.repository.UserRepository
import retrofit2.HttpException
import java.io.IOException

class UserRepositoryImplementation(
    private val api: UserAPI
): UserRepository {

    override suspend fun putUserInfo(
        token: String,
        dni: String,
        userCompleteDTO: UserCompleteDTO
    ): Resource<AuthUserDTO> {
        return try {
            val result =
                api.putUserInfo(token, dni, userCompleteDTO).data
            Resource.Success(data = result)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                val result =
                    api.putUserInfo(token, dni, userCompleteDTO).data
                Resource.Success(data = result)
            } else {
                Resource.Error(message = e.localizedMessage ?: "An unexpected error")
            }
        } catch (e: IOException) {
            Resource.Error(message = "Couldn't reach server")
        }
    }

}