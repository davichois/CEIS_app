package com.davichois.ceis.domain.use_case.user

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.UserDTO
import com.davichois.ceis.domain.repository.PreferencesRepository
import com.davichois.ceis.domain.repository.UserRepository
import javax.inject.Inject

class GetUserForDNIUseCase @Inject constructor(
    private val repository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(): Resource<UserDTO>{
        val dni = preferencesRepository.getUserPreferences().dni
        return repository.getUserForDNI(token = "eyJ0b2tlbiI6ImJyaXRlQXBwIn0", dni = dni)
    }

}