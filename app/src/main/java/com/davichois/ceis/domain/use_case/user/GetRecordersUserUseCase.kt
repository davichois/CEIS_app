package com.davichois.ceis.domain.use_case.user

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.domain.repository.PreferencesRepository
import com.davichois.ceis.domain.repository.UserRepository
import javax.inject.Inject

class GetRecordersUserUseCase @Inject constructor(
    private val repository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(day: String, dni: String = ""): List<EventModel>{
        val dniOperator = preferencesRepository.getUserPreferences().dni
        return repository.getRecordsUser(dni = dni.ifBlank { dniOperator }, day = day)
    }

}