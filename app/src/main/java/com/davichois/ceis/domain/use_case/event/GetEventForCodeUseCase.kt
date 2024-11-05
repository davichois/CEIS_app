package com.davichois.ceis.domain.use_case.event

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.domain.repository.EventRepository
import javax.inject.Inject

class GetEventForCodeUseCase @Inject constructor(
    private val repository: EventRepository,
) {

    suspend operator fun invoke(code: String): Resource<EventDTO> {
        return repository.getEventForCode(token = "eyJ0b2tlbiI6ImJyaXRlQXBwIn0", code = code)
    }

}