package com.davichois.ceis.presentation.event_management.adapter.choose_event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davichois.ceis.R
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.presentation.event_management.dto_pru.Evento
import com.davichois.ceis.presentation.login_management.adapter.avatar.AvatarManagementViewHolder

class ChooseEventAdapter(
    private val events: List<EventModel>,
    private val onCheckedChangeListener: (EventModel, Boolean) -> Unit
): RecyclerView.Adapter<ChooseEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseEventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChooseEventViewHolder(layoutInflater.inflate(R.layout.item_event_selected, parent, false))
    }

    override fun onBindViewHolder(holder: ChooseEventViewHolder, position: Int) {
        val evento = events[position]
        holder.render(evento) { isChecked ->
            onCheckedChangeListener(evento, isChecked)
        }
    }

    override fun getItemCount(): Int = events.size

}