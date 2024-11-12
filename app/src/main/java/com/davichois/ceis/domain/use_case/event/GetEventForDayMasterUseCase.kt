package com.davichois.ceis.domain.use_case.event

import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.domain.repository.EventRepository
import javax.inject.Inject

class GetEventForDayMasterUseCase @Inject constructor(
    private val repository: EventRepository
) {

    suspend operator fun invoke(day: String): Resource<List<EventModel>> {
        return repository.getEventForDayMaster(token = "eyJ0b2tlbiI6ImJyaXRlQXBwIn0", day = day)
    }

}