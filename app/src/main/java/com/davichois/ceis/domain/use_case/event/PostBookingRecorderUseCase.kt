package com.davichois.ceis.domain.use_case.event

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AttendanceRegisterDTO
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.domain.repository.EventRepository
import com.davichois.ceis.domain.repository.PreferencesRepository
import javax.inject.Inject

class PostBookingRecorderUseCase @Inject constructor(
    private val repository: EventRepository,
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(event: List<EventModel>): Resource<String> {
        val dni = preferencesRepository.getUserPreferences().dni
        return repository.postBookingRecorder(token = "eyJ0b2tlbiI6ImJyaXRlQXBwIn0", dni = dni, event = event)
    }

}