package com.davichois.ceis.domain.use_case.auth

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.domain.repository.AuthRepository
import javax.inject.Inject

class PostAuthenticateUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(dni: String): Resource<AuthUserDTO> {
        return repository.postAuthenticate(dni = dni)
    }

}