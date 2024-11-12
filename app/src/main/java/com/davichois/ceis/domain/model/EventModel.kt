package com.davichois.ceis.domain.model

import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.data.remote.dto.SpeakerEventDTO

data class EventModel (
    val id: Long,
    val name: String?,
    val date: String,
    val endDate: String,
    val place: String,
    val code: String,
    val modality: String,
    val typeEvent: String,
    val status: Boolean,
    val quota: Long,
    val speakers: List<SpeakerEventDTO>,
    val father: Any? = null,
    var isSelected: Boolean = false
)

fun EventModel.toEventDTO(): EventDTO {
    return EventDTO(
        id, name, date, endDate, place, code, modality, typeEvent, status, quota, speakers, father
    )
}
