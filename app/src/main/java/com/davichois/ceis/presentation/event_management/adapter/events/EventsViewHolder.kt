package com.davichois.ceis.presentation.event_management.adapter.events

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.davichois.ceis.R
import com.davichois.ceis.databinding.ItemEventBinding
import com.davichois.ceis.domain.model.EventModel
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventsViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemEventBinding.bind(view)

    @RequiresApi(Build.VERSION_CODES.O)
    fun render(item: EventModel, onSelectedEvent: (String) -> Unit) {
        val eventDateStart = getHourFromDateTime(item.date)
        val eventDateEnd = getHourFromDateTime(item.endDate)
        val concatDate = "$eventDateStart - $eventDateEnd"

        binding.eventTitle.text = item.name
        binding.vocationPerson.text = item.speakers[0].function
        binding.eventDateTime.text = concatDate

        if (item.speakers[0].image.isNullOrBlank()){
            binding.ivAvatarSpeaker.setImageResource(R.drawable.not_user)
        } else {
            Picasso.get().load(item.speakers[0].image).into(binding.ivAvatarSpeaker)
        }

        itemView.setOnClickListener {
            onSelectedEvent(item.code)
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