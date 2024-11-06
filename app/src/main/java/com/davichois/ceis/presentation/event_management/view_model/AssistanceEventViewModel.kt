package com.davichois.ceis.presentation.event_management.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.data.remote.dto.GenerateCodeDTO
import com.davichois.ceis.domain.use_case.event.GetGenerateCodeAssistanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AssistanceEventViewModel @Inject constructor(
    private val getGenerateCodeAssistanceUseCase: GetGenerateCodeAssistanceUseCase
): ViewModel() {

    private val _uiStateCodeGenerate = MutableStateFlow<Resource<GenerateCodeDTO>>(Resource.PreLoad)
    val uiStateCodeGenerate: StateFlow<Resource<GenerateCodeDTO>> = _uiStateCodeGenerate

    fun getGenerateCodeAssistanceForDNI(code: String, dni: String) {
        viewModelScope.launch {
            _uiStateCodeGenerate.value = Resource.Loading
            try {
                when(val result = getGenerateCodeAssistanceUseCase(code = code, dni = dni)) {
                    is Resource.Success -> {
                        _uiStateCodeGenerate.value = Resource.Success(data = result.data)
                    }
                    is Resource.Error -> {
                        _uiStateCodeGenerate.value = Resource.Error("Error event for code")
                    }

                    else -> {
                        _uiStateCodeGenerate.value = Resource.Loading
                    }
                }
            } catch (e: IOException) {
                _uiStateCodeGenerate.value = Resource.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

}