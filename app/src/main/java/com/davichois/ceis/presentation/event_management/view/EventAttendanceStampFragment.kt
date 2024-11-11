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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.davichois.ceis.R
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.databinding.FragmentEventAttendanceStampBinding
import com.davichois.ceis.presentation.event_management.view_model.AssistanceEventViewModel
import com.davichois.ceis.presentation.event_management.view_model.EventManagementViewModel
import com.davichois.ceis.presentation.login_management.view.LoginInAppFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventAttendanceStampFragment : Fragment(R.layout.fragment_event_attendance_stamp) {

    private var _binding: FragmentEventAttendanceStampBinding? = null
    private val binding get() = _binding

    // Injection view model
    private val eventManagementViewModel: EventManagementViewModel by viewModels()
    private val assistanceEventViewModel: AssistanceEventViewModel by viewModels()

    // args - view
    private val args: EventAttendanceStampFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventAttendanceStampBinding.inflate(
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

        eventManagementViewModel.getEventForCode(args.eventCode)

        binding?.btnSeal?.setOnClickListener {
            assistanceEventViewModel.postAttendanceRecorder(args.eventCode)
        }

        lifecycleScope.launch {
            eventManagementViewModel.uiStateEventForCode.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        Toast.makeText(requireActivity(), "cargando", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Success -> {
                        // Toast.makeText(requireActivity(), "correct", Toast.LENGTH_LONG).show()
                        // Paint information on labels
                        val data = state.data
                        val speakerPrincipal = state.data.speakers[0]

                        binding?.backStage?.setOnClickListener {
                            findNavController().popBackStack()
                        }
                        binding?.eventTitle?.text = data.name
                        binding?.nameSpeaker?.text = speakerPrincipal.name
                        "${speakerPrincipal.degree}, ${speakerPrincipal.function}".also { binding?.vocationPerson?.text = it }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireActivity(), state.message, Toast.LENGTH_LONG).show()

                    }

                    Resource.PreLoad -> Toast.makeText(requireActivity(), "Precargando elecciones", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

}