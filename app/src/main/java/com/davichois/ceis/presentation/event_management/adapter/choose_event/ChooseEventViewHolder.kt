package com.davichois.ceis.presentation.event_management.adapter.choose_event

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.davichois.ceis.databinding.ItemEventSelectedBinding
import com.davichois.ceis.domain.model.EventModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChooseEventViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemEventSelectedBinding.bind(view)

    fun render(evento: EventModel, onCheckedChangeListener: (Boolean) -> Unit) {
        val eventDateStart = getHourFromDateTime(evento.date)
        val eventDateEnd = getHourFromDateTime(evento.endDate)

        val concatDate = "$eventDateStart - $eventDateEnd"

        // Establecer el color según el tipo de evento
        when (evento.typeEvent) {
            "COLOQUIO" -> binding.typeEventPoint.setColorFilter(Color.parseColor("#EF5350"))
            "SESIONES_PARALELAS" -> binding.typeEventPoint.setColorFilter(Color.parseColor("#29B6F6"))
            else -> binding.typeEventPoint.setColorFilter(Color.parseColor("#EF5350"))
        }

        // Asignar los valores a los otros campos
        binding.quotaEvent.text = evento.quota.toString()
        binding.eventTitle.text = evento.name
        binding.placeEvent.text = evento.place
        binding.eventDateTime.text = concatDate

        // Establecer el estado de la casilla de verificación según el modelo de datos
        binding.checkboxEvento.isChecked = evento.isSelected

        // Configurar el listener para manejar el cambio en la casilla de verificación
        binding.checkboxEvento.setOnCheckedChangeListener { _, isChecked ->
            // Actualizar el modelo con el nuevo estado
            evento.isSelected = isChecked
            // Notificar al adaptador para que se actualice el estado
            onCheckedChangeListener(isChecked)
        }
    }


    fun getHourFromDateTime(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
        return dateTime.format(hourFormatter)
    }

}