package com.davichois.ceis.domain.use_case.event

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.GenerateCodeDTO
import com.davichois.ceis.domain.repository.EventRepository
import com.davichois.ceis.domain.repository.PreferencesRepository
import javax.inject.Inject

class GetGenerateCodeAssistanceUseCase @Inject constructor(
    private val repository: EventRepository,
) {

    suspend operator fun invoke(code: String, dni: String): Resource<GenerateCodeDTO> {
        return repository.getGenerateCodeAssistance(token = "eyJ0b2tlbiI6ImJyaXRlQXBwIn0", dni = dni, code = code)
    }

}