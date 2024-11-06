package com.davichois.ceis.presentation.event_management.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.davichois.ceis.R
import com.davichois.ceis.core.util.hideKeyboard
import com.davichois.ceis.core.util.onTextChange
import com.davichois.ceis.databinding.FragmentGenerateCodeAssistanceBinding
import com.davichois.ceis.presentation.event_management.view_model.AssistanceEventViewModel
import dagger.hilt.android.AndroidEntryPoint

// Fragment not use
@AndroidEntryPoint
class GenerateCodeAssistanceFragment : Fragment(R.layout.fragment_generate_code_assistance) {

    private var _binding: FragmentGenerateCodeAssistanceBinding? = null
    private val binding get() = _binding

    // Injection view model
    private val assistanceEventViewModel: AssistanceEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenerateCodeAssistanceBinding.inflate(
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

        binding?.tfDni?.onTextChange { s ->
            binding?.btnGenerateCode?.isEnabled = s.length >= 8
        }
        binding?.btnGenerateCode?.setOnClickListener {
            hideKeyboard()
            val dni = binding?.tfDni?.text.toString().trim()
            //loginManagementViewModel.authenticateUser(dni)
        }
    }

}