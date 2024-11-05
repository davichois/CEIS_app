package com.davichois.ceis.data.repository

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.EventAPI
import com.davichois.ceis.data.remote.dto.AttendanceRegisterDTO
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.data.remote.dto.GenerateCodeDTO
import com.davichois.ceis.domain.repository.EventRepository
import retrofit2.HttpException
import java.io.IOException

class EventRepositoryImplementation(
    private val api: EventAPI
): EventRepository {

    override suspend fun getGenerateCodeAssistance(
        token: String,
        dni: String,
        code: String
    ): Resource<GenerateCodeDTO> {
        return try {
            val result =
                api.getGenerateCodeAssistance(token, dni, code).data
            Resource.Success(data = result)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                val result =
                    api.getGenerateCodeAssistance(token, dni, code).data
                Resource.Success(data = result)
            } else {
                Resource.Error(message = e.localizedMessage ?: "An unexpected error")
            }
        } catch (e: IOException) {
            Resource.Error(message = "Couldn't reach server")
        }
    }

    override suspend fun postAttendanceRecorder(
        token: String,
        dni: String,
        code: String
    ): Resource<AttendanceRegisterDTO> {
        return try {
            val result =
                api.postAttendanceRecorder(token, dni, code).data
            Resource.Success(data = result)
        } catch (e: HttpException) {
            when(e.code()){
                401 -> {
                    val result =
                        api.postAttendanceRecorder(token, dni, code).data
                    Resource.Success(data = result)
                }
                300 -> {
                    Resource.Error(message = e.localizedMessage ?: "Duplicate registration")
                }
                else -> Resource.Error(message = e.localizedMessage ?: "An unexpected error")
            }
        } catch (e: IOException) {
            Resource.Error(message = "Couldn't reach server")
        }
    }

    override suspend fun postCodeSupportRecorder(
        token: String,
        codeAssistance: String
    ): Resource<AttendanceRegisterDTO> {
        return try {
            val result =
                api.postCodeSupportRecorder(token, codeAssistance).data
            Resource.Success(data = result)
        } catch (e: HttpException) {
            when(e.code()){
                401 -> {
                    val result =
                        api.postCodeSupportRecorder(token, codeAssistance).data
                    Resource.Success(data = result)
                }
                300 -> {
                    Resource.Error(message = e.localizedMessage ?: "Duplicate registration")
                }
                else -> Resource.Error(message = e.localizedMessage ?: "An unexpected error")
            }
        } catch (e: IOException) {
            Resource.Error(message = "Couldn't reach server")
        }
    }

    override suspend fun getEventForCode(token: String, code: String): Resource<EventDTO> {
        return try {
            val result =
                api.getEventForCode(token, code).data
            Resource.Success(data = result)
        } catch (e: HttpException) {
            when(e.code()){
                401 -> {
                    val result =
                        api.getEventForCode(token, code).data
                    Resource.Success(data = result)
                }
                else -> Resource.Error(message = e.localizedMessage ?: "An unexpected error")
            }
        } catch (e: IOException) {
            Resource.Error(message = "Couldn't reach server")
        }
    }

    override suspend fun getEventForDay(token: String, day: String): Resource<List<EventDTO>> {
        return try {
            val result =
                api.getEventForDay(token, day).data
            Resource.Success(data = result)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                val result =
                    api.getEventForDay(token, day).data
                Resource.Success(data = result)
            } else {
                Resource.Error(message = e.localizedMessage ?: "An unexpected error")
            }
        } catch (e: IOException) {
            Resource.Error(message = "Couldn't reach server")
        }
    }

    override suspend fun getEventForAll(token: String): Resource<Map<String, List<EventDTO>>> {
        return try {
            val result =
                api.getEventForAll(token).data
            Resource.Success(data = result)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                val result =
                    api.getEventForAll(token).data
                Resource.Success(data = result)
            } else {
                Resource.Error(message = e.localizedMessage ?: "An unexpected error")
            }
        } catch (e: IOException) {
            Resource.Error(message = "Couldn't reach server")
        }
    }

}