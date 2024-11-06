package com.davichois.ceis.presentation.login_management.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.davichois.ceis.R
import com.davichois.ceis.databinding.FragmentBasicInformationBinding
import com.davichois.ceis.presentation.login_management.adapter.avatar.AvatarManagementAdapter
import com.davichois.ceis.presentation.login_management.dto.Avatar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

// Fragment not use
@AndroidEntryPoint
class BasicInformationFragment : Fragment(R.layout.fragment_basic_information) {

    private var _binding: FragmentBasicInformationBinding? = null
    private val binding get() = _binding

    private var avatarList: List<Avatar> = listOf(
        Avatar(name = "betoo", isSelected = false, gender = "MALE"),
        Avatar(name = "andrea", isSelected = false, gender = "FEMALE")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBasicInformationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.statusBarColor = Color.rgb(237,241,253)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvListAvatars?.layoutManager = manager

        // Asegúrate de que el adaptador esté configurado con el callback correcto
        binding?.rvListAvatars?.adapter = AvatarManagementAdapter(avatarList) { avatar ->
            putAvatarSelected(avatar)
        }
    }

    private fun putAvatarSelected(avatar: String) {
        val name = avatar.split(",")[0]
        val gender = avatar.split(",")[1]
        Toast.makeText(requireActivity(), "Avatar seleccionado: $avatar", Toast.LENGTH_LONG).show()
        selectAvatarByName(name)
        initRecyclerView()
    }

    private fun selectAvatarByName(name: String) {
        avatarList = avatarList.map { avatar ->
            avatar.copy(isSelected = avatar.name == name)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Para evitar fugas de memoria
    }
}
