package com.davichois.ceis.presentation.login_management.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.domain.use_case.auth.PostAuthenticateUseCase
import com.davichois.ceis.domain.use_case.preferences.GetUserPreferenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginManagementViewModel @Inject constructor(
    private val postAuthenticateUseCase: PostAuthenticateUseCase,
    private val getUserPreferenceUseCase: GetUserPreferenceUseCase
): ViewModel() {

    private val _uiStateLogin = MutableStateFlow<Resource<AuthUserDTO>>(Resource.PreLoad)
    val uiStateLogin: StateFlow<Resource<AuthUserDTO>> = _uiStateLogin

    private val _uiStateNavigate = MutableStateFlow<Resource<Int>>(Resource.PreLoad)
    val uiStateNavigate: StateFlow<Resource<Int>> = _uiStateNavigate

    fun authenticateUser(dni: String) {
        viewModelScope.launch {
            _uiStateLogin.value = Resource.Loading
            delay(1000)
            try {
                when(val result = postAuthenticateUseCase(dni = dni)) {
                    is Resource.Success -> {
                        _uiStateLogin.value = Resource.Success(data = result.data)
                    }
                    is Resource.Error -> {
                        _uiStateLogin.value = Resource.Error("Error al login")
                    }

                    else -> {
                        _uiStateLogin.value = Resource.Loading
                    }
                }
            } catch (e: IOException) {
                _uiStateLogin.value = Resource.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

    fun navigateForUse(day: String) {
        viewModelScope.launch {
            _uiStateNavigate.value = Resource.Loading
            try {
                val preferencesMap = mapOf(
                    "06" to getUserPreferenceUseCase().dia12,
                    "13" to getUserPreferenceUseCase().dia13,
                    "14" to getUserPreferenceUseCase().dia14,
                    "15" to getUserPreferenceUseCase().dia15
                )

                _uiStateNavigate.value = preferencesMap[day]?.let { preference ->
                    Resource.Success(data = if (preference) 2 else 1)
                } ?: Resource.Error("NOT DAY - CEIS FINISH")
            } catch (e: IOException) {
                _uiStateLogin.value = Resource.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

}