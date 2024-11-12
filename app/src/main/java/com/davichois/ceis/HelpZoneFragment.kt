package com.davichois.ceis

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.databinding.FragmentHelpZoneBinding
import com.davichois.ceis.presentation.login_management.view.LoginInAppFragmentDirections
import com.davichois.ceis.presentation.login_management.view_model.LoginManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class HelpZoneFragment : Fragment(R.layout.fragment_help_zone) {

    private var _binding: FragmentHelpZoneBinding? = null
    private val binding get() = _binding

    // Injection view model
    private val loginManagementViewModel: LoginManagementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHelpZoneBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.statusBarColor = Color.rgb(237, 241, 253)

        // Obtencion de fecha de evento y guardado
        // Array con fechas específicas (año, mes, día) CEIS
        val datesArray = arrayOf(
            Calendar.getInstance().apply { set(2024, 10, 12)},
            Calendar.getInstance().apply { set(2024, 10, 13) },
            Calendar.getInstance().apply { set(2024, 10, 14) },
            Calendar.getInstance().apply { set(2024, 10, 15) }
        )
        val startDate = Calendar.getInstance().apply { set(2024, 10, 12) }
        val endDate = Calendar.getInstance().apply { set(2024, 10, 15) }

        val currentTime = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentTime.time)
        val dayCurrent = formattedDate.split("/")[0].trim()

        when {
            currentTime in datesArray -> {
                // Toast.makeText(requireActivity(), "Bienvenido! $dayCurrent", Toast.LENGTH_LONG).show()
            }
            currentTime.after(endDate) -> {
                //Toast.makeText(requireActivity(), "Día no hábil", Toast.LENGTH_LONG).show()
            }
            currentTime.before(startDate) -> {
                //Toast.makeText(requireActivity(), "Aún no se ha aperturado", Toast.LENGTH_LONG).show()
            }
        }

        // TODO: days change
        loginManagementViewModel.getVerifiedAccountInAppInitialized(dayCurrent)

        lifecycleScope.launch {
            delay(2000)
            loginManagementViewModel.uiStateNavigateSplash.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        // Toast.makeText(requireActivity(), "cargando", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Success -> {
                        when(state.data) {
                            1 ->{
                                val action = HelpZoneFragmentDirections.actionHelpZoneFragmentToHomeChooseEventFragment()
                                findNavController().navigate(action)
                            }
                            2 ->{
                                val action = HelpZoneFragmentDirections.actionHelpZoneFragmentToLoginInAppFragment()
                                findNavController().navigate(action)
                            }
                            3 ->{
                                val action = HelpZoneFragmentDirections.actionHelpZoneFragmentToHomeEventFragment()
                                findNavController().navigate(action)
                            }
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireActivity(), state.message, Toast.LENGTH_LONG).show()

                    }

                    Resource.PreLoad -> {
                        println("Precargando elecciones",)
                        /*Toast.makeText(
                            requireActivity(),
                            "Precargando elecciones",
                            Toast.LENGTH_LONG
                        ).show()*/
                    }
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
