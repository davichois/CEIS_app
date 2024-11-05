package com.davichois.ceis.presentation.event_management.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.davichois.ceis.R
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.databinding.FragmentDetailsChosenEventBinding
import com.davichois.ceis.presentation.event_management.view_model.EventManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsChosenEventFragment : Fragment(R.layout.fragment_details_chosen_event) {

    private var _binding: FragmentDetailsChosenEventBinding? = null
    private val binding get() = _binding

    // Injection view model
    private val eventManagementViewModel: EventManagementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsChosenEventBinding.inflate(
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

        lifecycleScope.launch {
            eventManagementViewModel.uiStateEventForCode.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        Toast.makeText(requireActivity(), "cargando", Toast.LENGTH_LONG).show()

                    }
                    is Resource.Success -> {
                        Toast.makeText(requireActivity(), "correcto", Toast.LENGTH_LONG).show()

                    }
                    is Resource.Error -> {
                        Toast.makeText(requireActivity(), "error", Toast.LENGTH_LONG).show()
                    }

                    Resource.PreLoad -> Toast.makeText(requireActivity(), "Bienvenido", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

}