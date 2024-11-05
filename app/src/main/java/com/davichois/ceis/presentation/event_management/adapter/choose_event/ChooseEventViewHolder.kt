package com.davichois.ceis.presentation.event_management.adapter.choose_event

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.databinding.ItemEventSelectedBinding
import com.davichois.ceis.presentation.event_management.dto_pru.Evento

class ChooseEventViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemEventSelectedBinding.bind(view)

    fun render(evento: Evento, onCheckedChangeListener: (Boolean) -> Unit) {
        binding.checkboxEvento.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChangeListener(isChecked)
        }
    }
    
}