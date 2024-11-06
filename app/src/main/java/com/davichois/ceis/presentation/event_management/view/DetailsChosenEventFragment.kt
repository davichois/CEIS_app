package com.davichois.ceis.presentation.event_management.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.davichois.ceis.R
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.core.util.hideKeyboard
import com.davichois.ceis.core.util.onTextChange
import com.davichois.ceis.databinding.FragmentDetailsChosenEventBinding
import com.davichois.ceis.databinding.FragmentGenerateCodeAssistanceBinding
import com.davichois.ceis.presentation.event_management.dialog.CodeQrDialog
import com.davichois.ceis.presentation.event_management.view_model.AssistanceEventViewModel
import com.davichois.ceis.presentation.event_management.view_model.EventManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsChosenEventFragment : Fragment(R.layout.fragment_details_chosen_event) {

    private var _binding: FragmentDetailsChosenEventBinding? = null
    private val binding get() = _binding

    // Injection view model
    private val eventManagementViewModel: EventManagementViewModel by viewModels()
    private val assistanceEventViewModel: AssistanceEventViewModel by viewModels()

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

        binding?.ivCodeComputerMaster?.setOnClickListener {
            showViewGenerateCode("C3I5ZmH")
        }
        binding?.ivQrGenerateMaster?.setOnClickListener {
            generateQrForAttendance("C3I5ZmH")
        }
        binding?.ivCodeAlphaMaster?.setOnClickListener {
            countDownCodePreview()
        }

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

                    Resource.PreLoad -> {
                        println("Reload")
                        // Toast.makeText(requireActivity(), "Bienvenido", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    private fun showViewGenerateCode(codeEvent: String) {
        val dialog = Dialog(requireActivity(), android.R.style.Theme_Material_NoActionBar_Fullscreen)
        val view = layoutInflater.inflate(R.layout.fragment_generate_code_assistance, null)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.startAnimation(
            AnimationUtils.loadAnimation(
                requireActivity(),
                R.anim.fade_in
            ))

        val bindingDialog = FragmentGenerateCodeAssistanceBinding.bind(view)

        bindingDialog.tfDni.onTextChange { s ->
            bindingDialog.btnGenerateCode.isEnabled = s.length >= 8
        }
        bindingDialog.btnGenerateCode.setOnClickListener {
            hideKeyboard()
            val dni = bindingDialog.tfDni.text.toString().trim()
            assistanceEventViewModel.getGenerateCodeAssistanceForDNI(code = codeEvent, dni = dni)
        }
        bindingDialog.backStage.setOnClickListener {
            view.startAnimation(
                AnimationUtils.loadAnimation(
                    requireActivity(),
                    R.anim.fade_in
                )
            )
            dialog.dismiss()
        }

        lifecycleScope.launch {
            assistanceEventViewModel.uiStateCodeGenerate.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        Toast.makeText(requireActivity(), "Generando código.", Toast.LENGTH_LONG).show()

                    }
                    is Resource.Success -> {
                        // Toast.makeText(requireActivity(), state.data.code, Toast.LENGTH_LONG).show()
                        bindingDialog.codeGenerate.text = state.data.code

                    }
                    is Resource.Error -> {
                        Toast.makeText(requireActivity(), "Paso un error inesperado", Toast.LENGTH_LONG).show()
                    }

                    Resource.PreLoad -> {
                        println("Reload")
                        // Toast.makeText(requireActivity(), "Bienvenido", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun generateQrForAttendance(codeEvent: String) {
        CodeQrDialog(
            codeQr = codeEvent
        ).show(parentFragmentManager, "qrCode")
    }

    private fun countDownCodePreview() {
        val fiveMinutesInMillis: Long = 1 * 60 * 1000

        val timer = object : CountDownTimer(fiveMinutesInMillis, 1000) {
            override fun onTick(p0: Long) {
                var diff = p0

                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = diff / daysInMilli
                diff %= daysInMilli

                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

                binding?.codeEvent?.visibility = View.VISIBLE
                binding?.codeEvent?.text = "C3I5G04"

            }

            override fun onFinish() {
                binding?.codeEvent?.visibility = View.INVISIBLE
            }
        }

        timer.start()
    }

}