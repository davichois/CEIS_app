package com.davichois.ceis.domain.use_case.user

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.domain.repository.PreferencesRepository
import com.davichois.ceis.domain.repository.UserRepository
import javax.inject.Inject

class GetRecordersUserControlledUseCase @Inject constructor(
    private val repository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(day: String, dni: String = ""): Resource<List<EventModel>>{
        val dniOperator = preferencesRepository.getUserPreferences().dni
        return repository.getRecordsUserControlled(dni = dni.ifBlank { dniOperator }, day = day)
    }

}