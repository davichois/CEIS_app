package com.davichois.ceis.domain.use_case.event

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.domain.repository.EventRepository
import javax.inject.Inject

class GetEventForAllUseCase @Inject constructor(
    private val repository: EventRepository
) {

    suspend operator fun invoke(): Resource<Map<String, List<EventDTO>>> {
        return repository.getEventForAll(token = "eyJ0b2tlbiI6ImJyaXRlQXBwIn0")
    }

}