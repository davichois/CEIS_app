package com.davichois.ceis.presentation.login_management.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.domain.use_case.auth.PostAuthenticateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginManagementViewModel @Inject constructor(
    private val postAuthenticateUseCase: PostAuthenticateUseCase
): ViewModel() {

    private val _uiStateLogin = MutableStateFlow<Resource<AuthUserDTO>>(Resource.PreLoad)
    val uiStateLogin: StateFlow<Resource<AuthUserDTO>> = _uiStateLogin

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

}