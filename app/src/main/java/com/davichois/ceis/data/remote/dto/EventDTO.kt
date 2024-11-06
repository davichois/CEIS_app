package com.davichois.ceis.data.remote.dto

import com.davichois.ceis.domain.model.EventModel

data class EventDTO (
    val id: Long,
    val name: String,
    val date: String,
    val endDate: String,
    val place: String,
    val code: String,
    val modality: String,
    val typeEvent: String,
    val status: Boolean,
    val quota: Long,
    val speakers: List<SpeakerEventDTO>,
    val father: Any? = null
)

fun EventDTO.toEventModel(): EventModel {
    return EventModel(
        id, name, date, endDate, place, code, modality, typeEvent, status, quota, speakers, father, isSelected = false
    )
}
