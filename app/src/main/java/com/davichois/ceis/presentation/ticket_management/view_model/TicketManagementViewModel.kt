package com.davichois.ceis.presentation.ticket_management.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.UserDTO
import com.davichois.ceis.domain.use_case.user.GetUserForDNIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TicketManagementViewModel @Inject constructor(
    private val getUserForDNIUseCase: GetUserForDNIUseCase
): ViewModel() {

    private val _uiStateUser = MutableStateFlow<Resource<UserDTO>>(Resource.PreLoad)
    val uiStateUser: StateFlow<Resource<UserDTO>> = _uiStateUser

    fun getUserForDNI() {
        viewModelScope.launch {
            _uiStateUser.value = Resource.Loading
            delay(2000)
            try {
                when(val result = getUserForDNIUseCase()) {
                    is Resource.Success -> {
                        _uiStateUser.value = Resource.Success(result.data)
                    }
                    is Resource.Error -> {
                        _uiStateUser.value = Resource.Error("Error al traer a usuario")
                    }

                    else -> {
                        _uiStateUser.value = Resource.Loading
                    }
                }
            } catch (e: IOException) {
                _uiStateUser.value = Resource.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

}