package com.davichois.ceis.presentation.login_management.view

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
import com.davichois.ceis.databinding.FragmentLoginInAppBinding
import com.davichois.ceis.presentation.login_management.view_model.LoginManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class LoginInAppFragment : Fragment(R.layout.fragment_login_in_app) {

    private var _binding: FragmentLoginInAppBinding? = null
    private val binding get() = _binding

    // Injection view model
    private val loginManagementViewModel: LoginManagementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginInAppBinding.inflate(
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

        // TODO verified day current
        val currentTime = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentTime.time)
        val dayCurrent = formattedDate.split("/")[0].trim()

        binding?.tfDni?.onTextChange { s ->
            binding?.btnLogin?.isEnabled = s.length >= 8
        }
        binding?.btnLogin?.setOnClickListener {
            hideKeyboard()
            val dni = binding?.tfDni?.text.toString().trim()
            loginManagementViewModel.authenticateUser(dni, "13")
        }

        lifecycleScope.launch {
            loginManagementViewModel.uiStateLogin.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding?.contentLIA?.visibility = View.GONE
                        binding?.loadShimmerLIA?.visibility = View.VISIBLE
                        // Toast.makeText(requireActivity(), "cargando", Toast.LENGTH_LONG).show()

                    }
                    is Resource.Success -> {
                        binding?.contentLIA?.visibility = View.VISIBLE
                        binding?.loadShimmerLIA?.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        binding?.contentLIA?.visibility = View.VISIBLE
                        binding?.loadShimmerLIA?.visibility = View.GONE
                        Toast.makeText(requireActivity(), "error", Toast.LENGTH_LONG).show()
                    }

                    Resource.PreLoad -> Toast.makeText(requireActivity(), "Bienvenido", Toast.LENGTH_LONG).show()
                }
            }
        }

        lifecycleScope.launch {
            loginManagementViewModel.uiStateNavigate.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding?.contentLIA?.visibility = View.GONE
                        binding?.loadShimmerLIA?.visibility = View.VISIBLE
                        // Toast.makeText(requireActivity(), "cargando", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Success -> {
                        binding?.contentLIA?.visibility = View.VISIBLE
                        binding?.loadShimmerLIA?.visibility = View.GONE
                        when(state.data){
                            1 -> {
                                val action = LoginInAppFragmentDirections.actionLoginInAppFragmentToHomeChooseEventFragment()
                                findNavController().navigate(action)
                                //Toast.makeText(requireActivity(), "1", Toast.LENGTH_LONG).show()
                            }
                            2 -> {
                                val action = LoginInAppFragmentDirections.actionLoginInAppFragmentToHomeEventFragment()
                                findNavController().navigate(action)
                                //Toast.makeText(requireActivity(), "2", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                Toast.makeText(requireActivity(), "CEIS ya termino", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    is Resource.Error -> {
                        // REDIRECT IF CEIS FINISH
                        binding?.contentLIA?.visibility = View.VISIBLE
                        binding?.loadShimmerLIA?.visibility = View.GONE
                        Toast.makeText(requireActivity(), state.message, Toast.LENGTH_LONG).show()

                    }

                    Resource.PreLoad -> Toast.makeText(requireActivity(), "Precargando elecciones", Toast.LENGTH_LONG).show()
                }
            }

        }

    }

}