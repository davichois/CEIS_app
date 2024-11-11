package com.davichois.ceis.data.repository

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.UserAPI
import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.data.remote.dto.UserCompleteDTO
import com.davichois.ceis.data.remote.dto.UserDTO
import com.davichois.ceis.data.remote.dto.toEventModel
import com.davichois.ceis.domain.model.EventModel
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

    override suspend fun getRecordsUser(dni: String, day: String): List<EventModel> {
        return api.getRecordsUser(dni, day).data.map { eventDTO: EventDTO -> eventDTO.toEventModel() }
    }

    override suspend fun getRecordsUserControlled(
        dni: String,
        day: String
    ): Resource<List<EventModel>> {
        return try {
            val result =
                api.getRecordsUser(dni, day).data.map { eventDTO: EventDTO -> eventDTO.toEventModel() }
            Resource.Success(data = result)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                val result =
                    api.getRecordsUser(dni, day).data.map { eventDTO: EventDTO -> eventDTO.toEventModel() }
                Resource.Success(data = result)
            } else {
                Resource.Error(message = e.localizedMessage ?: "An unexpected error")
            }
        } catch (e: IOException) {
            Resource.Error(message = "Couldn't reach server")
        }
    }

    override suspend fun getUserForDNI(token: String, dni: String): Resource<UserDTO> {
        return try {
            val result =
                api.getUserForDNI(token, dni).data
            Resource.Success(data = result)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                val result =
                    api.getUserForDNI(token, dni).data
                Resource.Success(data = result)
            } else {
                Resource.Error(message = e.localizedMessage ?: "An unexpected error")
            }
        } catch (e: IOException) {
            Resource.Error(message = "Couldn't reach server")
        }
    }


}