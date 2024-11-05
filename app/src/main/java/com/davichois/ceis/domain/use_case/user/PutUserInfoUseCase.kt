package com.davichois.ceis.domain.use_case.user

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.data.remote.dto.UserCompleteDTO
import com.davichois.ceis.domain.repository.PreferencesRepository
import com.davichois.ceis.domain.repository.UserRepository
import javax.inject.Inject

class PutUserInfoUseCase @Inject constructor(
    private val repository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(userCompleteDTO: UserCompleteDTO): Resource<AuthUserDTO> {
        val dni = preferencesRepository.getUserPreferences().dni
        return repository.putUserInfo(token = "eyJ0b2tlbiI6ImJyaXRlQXBwIn0", dni = dni, userCompleteDTO = userCompleteDTO)
    }

}