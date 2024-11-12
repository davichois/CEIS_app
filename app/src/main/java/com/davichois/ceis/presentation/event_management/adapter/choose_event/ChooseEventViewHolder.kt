package com.davichois.ceis.presentation.event_management.adapter.choose_event

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.davichois.ceis.databinding.ItemEventSelectedBinding
import com.davichois.ceis.domain.model.EventModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChooseEventViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemEventSelectedBinding.bind(view)

    @RequiresApi(Build.VERSION_CODES.O)
    fun render(evento: EventModel, onCheckedChangeListener: (Boolean) -> Unit) {
        val eventDateStart = getHourFromDateTime(evento.date)
        val eventDateEnd = getHourFromDateTime(evento.endDate)

        val concatDate = "$eventDateStart - $eventDateEnd"

        when(evento.typeEvent) {
            "COLOQUIO" -> binding.typeEventPoint.setColorFilter(Color.parseColor("#EF5350"))
            "SESIONES_PARALELAS" -> binding.typeEventPoint.setColorFilter(Color.parseColor("#29B6F6"))

            else -> binding.typeEventPoint.setColorFilter(Color.parseColor("#EF5350"))
        }



        binding.quotaEvent.text = evento.quota.toString()
        binding.eventTitle.text = evento.name
        binding.placeEvent.text = evento.place
        binding.eventDateTime.text = concatDate

        binding.checkboxEvento.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChangeListener(isChecked)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHourFromDateTime(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
        return dateTime.format(hourFormatter)
    }
    
}