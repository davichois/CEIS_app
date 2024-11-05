package com.davichois.ceis.presentation.login_management.adapter.avatar

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.davichois.ceis.databinding.ItemAvatarBinding
import com.davichois.ceis.presentation.login_management.dto.Avatar

class AvatarManagementViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemAvatarBinding.bind(view)

    @SuppressLint("ClickableViewAccessibility")
    fun render(item: Avatar, onSelectedAvatar: (String) -> Unit) {
        binding.riveAnimation.setRiveResource(
            itemView.context.resources.getIdentifier(item.name, "raw", itemView.context.packageName)
        )
        binding.riveAnimation.isClickable = true
        binding.riveAnimation.isFocusable = true

        binding.selectedItem.visibility = if (item.isSelected) View.VISIBLE else View.GONE

        binding.riveAnimation.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                onSelectedAvatar("${item.name},${item.gender}")
                true
            } else {
                false
            }
        }
    }
}