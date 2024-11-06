package com.davichois.ceis.domain.use_case.event

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.domain.repository.EventRepository
import javax.inject.Inject

class GetEventForDayUseCase @Inject constructor(
    private val repository: EventRepository
) {

    suspend operator fun invoke(day: String, type: String): Resource<List<EventModel>> {
        return repository.getEventForDay(token = "eyJ0b2tlbiI6ImJyaXRlQXBwIn0", day = day, type = type)
    }

}