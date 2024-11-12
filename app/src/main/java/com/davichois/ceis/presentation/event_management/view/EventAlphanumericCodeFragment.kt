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
import com.davichois.ceis.R
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.core.util.hideKeyboard
import com.davichois.ceis.core.util.onTextChange
import com.davichois.ceis.databinding.FragmentEventAlphanumericCodeBinding
import com.davichois.ceis.presentation.event_management.view_model.AssistanceEventViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventAlphanumericCodeFragment : Fragment(R.layout.fragment_event_alphanumeric_code) {

    private var _binding: FragmentEventAlphanumericCodeBinding? = null
    private val binding get() = _binding

    // Injection view model
    private val assistanceEventViewModel: AssistanceEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventAlphanumericCodeBinding.inflate(
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

        binding?.backView?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.tfAlfanumerico?.onTextChange { s ->
            binding?.btnSeal?.isEnabled = s.length >= 6
        }
        binding?.btnSeal?.setOnClickListener {
            hideKeyboard()
            val codeEvent = binding?.tfAlfanumerico?.text.toString().trim()
            assistanceEventViewModel.postAttendanceRecorder(codeEvent)
        }

        lifecycleScope.launch {
            assistanceEventViewModel.uiStateAttendanceRecorder.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding?.contentLIA?.visibility = View.GONE
                        binding?.loadShimmerLIA?.visibility = View.VISIBLE
                        // Toast.makeText(requireActivity(), "cargando", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        findNavController().navigate(
                            EventAlphanumericCodeFragmentDirections.actionEventAlphanumericCodeFragmentToHomeEventFragment()
                        )
                    }
                    is Resource.Error -> {
                        binding?.contentLIA?.visibility = View.VISIBLE
                        binding?.loadShimmerLIA?.visibility = View.GONE
                        Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
                        println(state.message)
                    }

                    Resource.PreLoad -> {
                        println("start in app")
                        // Toast.makeText(requireActivity(), "Bienvenido", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}