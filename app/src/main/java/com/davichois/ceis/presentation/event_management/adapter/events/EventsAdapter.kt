package com.davichois.ceis.presentation.event_management.adapter.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davichois.ceis.R
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.presentation.login_management.adapter.avatar.AvatarManagementViewHolder

class EventsAdapter(
    private val events: List<EventModel>,
    private val onSelectedEvent: (String) -> Unit
): RecyclerView.Adapter<EventsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EventsViewHolder(layoutInflater.inflate(R.layout.item_event, parent, false))

    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val item = events[position]
        holder.render(item, onSelectedEvent)
    }

}