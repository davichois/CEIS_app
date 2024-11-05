package com.davichois.ceis.presentation.event_management.dto_pru

data class Evento(
    val id: Int,
    val tipo: String,
    val nombre: String,
    var isSelected: Boolean = false
)
