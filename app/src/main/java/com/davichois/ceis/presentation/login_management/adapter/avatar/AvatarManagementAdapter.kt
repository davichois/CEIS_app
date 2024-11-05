package com.davichois.ceis.presentation.login_management.adapter.avatar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davichois.ceis.R
import com.davichois.ceis.presentation.login_management.dto.Avatar

class AvatarManagementAdapter(
    private val avatarList: List<Avatar>,
    private val onSelectedAvatar: (String) -> Unit
): RecyclerView.Adapter<AvatarManagementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarManagementViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AvatarManagementViewHolder(layoutInflater.inflate(R.layout.item_avatar, parent, false))
    }

    override fun getItemCount(): Int = avatarList.size

    override fun onBindViewHolder(holder: AvatarManagementViewHolder, position: Int) {
        val item = avatarList[position]
        holder.render(item, onSelectedAvatar)
    }

}