package com.davichois.ceis.domain.repository

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.data.remote.dto.UserCompleteDTO
import com.davichois.ceis.data.remote.dto.UserDTO
import com.davichois.ceis.domain.model.EventModel

interface UserRepository {

    suspend fun putUserInfo(
        token: String,
        dni: String,
        userCompleteDTO: UserCompleteDTO,
    ): Resource<AuthUserDTO>

    suspend fun getRecordsUser(
        dni: String,
        day: String
    ): List<EventModel>

    suspend fun getRecordsUserControlled(
        dni: String,
        day: String
    ): Resource<List<EventModel>>

    suspend fun getUserForDNI(
        token: String,
        dni: String,
    ): Resource<UserDTO>

}