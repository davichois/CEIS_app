package com.davichois.ceis.domain.use_case.event

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AttendanceRegisterDTO
import com.davichois.ceis.domain.repository.EventRepository
import com.davichois.ceis.domain.repository.PreferencesRepository
import javax.inject.Inject

class PostAttendanceRecorderUseCase @Inject constructor(
    private val repository: EventRepository,
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(code: String, dni: String = ""): Resource<AttendanceRegisterDTO> {
        val dniOperator = preferencesRepository.getUserPreferences().dni
        return repository.postAttendanceRecorder(token = "eyJ0b2tlbiI6ImJyaXRlQXBwIn0", dni = dni.ifBlank { dniOperator }, code = code)
    }

}