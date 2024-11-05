package com.davichois.ceis.presentation.event_management.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.domain.use_case.event.GetEventForCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EventManagementViewModel @Inject constructor(
    private val getEventForCodeUseCase: GetEventForCodeUseCase
): ViewModel() {

    private val _uiStateEventForCode = MutableStateFlow<Resource<EventDTO>>(Resource.PreLoad)
    val uiStateEventForCode: StateFlow<Resource<EventDTO>> = _uiStateEventForCode

    fun getEventForCode(code: String) {
        viewModelScope.launch {
            _uiStateEventForCode.value = Resource.Loading
            try {
                when(val result = getEventForCodeUseCase(code = code)) {
                    is Resource.Success -> {
                        _uiStateEventForCode.value = Resource.Success(data = result.data)
                    }
                    is Resource.Error -> {
                        _uiStateEventForCode.value = Resource.Error("Error event for code")
                    }

                    else -> {
                        _uiStateEventForCode.value = Resource.Loading
                    }
                }
            } catch (e: IOException) {
                _uiStateEventForCode.value = Resource.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

}