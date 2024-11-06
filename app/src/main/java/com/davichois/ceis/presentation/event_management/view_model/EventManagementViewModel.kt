package com.davichois.ceis.presentation.event_management.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.domain.use_case.event.GetEventForCodeUseCase
import com.davichois.ceis.domain.use_case.event.GetEventForDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EventManagementViewModel @Inject constructor(
    private val getEventForCodeUseCase: GetEventForCodeUseCase,
    private val getEventForDayUseCase: GetEventForDayUseCase
): ViewModel() {

    private val _uiStateEventForCode = MutableStateFlow<Resource<EventDTO>>(Resource.PreLoad)
    val uiStateEventForCode: StateFlow<Resource<EventDTO>> = _uiStateEventForCode

    private val _uiStateListEventChooseDay = MutableStateFlow<Resource<List<EventModel>>>(Resource.PreLoad)
    val uiStateListEventChooseDay: StateFlow<Resource<List<EventModel>>> = _uiStateListEventChooseDay

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

    fun getEventForChooseForDay(day: String) {
        viewModelScope.launch {
            _uiStateListEventChooseDay.value = Resource.Loading

            try {
                val resultEventSPDeferred = async { getEventForDayUseCase(day = day, type = "SESIONES_PARALELAS") }
                val resultEventColoquioDeferred = async { getEventForDayUseCase(day = day, type = "COLOQUIO") }
                val resultEventTalleresDeferred = async { getEventForDayUseCase(day = day, type = "TALLERES") }

                val resultEventSP = resultEventSPDeferred.await()
                val resultEventColoquio = resultEventColoquioDeferred.await()
                val resultEventTalleres = resultEventTalleresDeferred.await()

                // Variables para combinar los resultados exitosos y capturar errores
                val combinedData = mutableListOf<EventModel>()  // Reemplaza YourDataType con el tipo correcto
                val errorMessages = mutableListOf<String>()

                // Procesa cada resultado
                if (resultEventSP is Resource.Success) {
                    combinedData += resultEventSP.data
                } else if (resultEventSP is Resource.Error) {
                    errorMessages.add("Error en SESIONES_PARALELAS: ${resultEventSP.message}")
                }

                if (resultEventColoquio is Resource.Success) {
                    combinedData += resultEventColoquio.data
                } else if (resultEventColoquio is Resource.Error) {
                    errorMessages.add("Error en COLOQUIO: ${resultEventColoquio.message}")
                }

                if (resultEventTalleres is Resource.Success) {
                    combinedData += resultEventTalleres.data
                } else if (resultEventTalleres is Resource.Error) {
                    errorMessages.add("Error en TALLERES: ${resultEventTalleres.message}")
                }

                // Decide el estado final en función de los resultados
                _uiStateListEventChooseDay.value = when {
                    errorMessages.isNotEmpty() && combinedData.isNotEmpty() -> {
                        Resource.Success(combinedData)
                    }
                    combinedData.isNotEmpty() -> {
                        Resource.Success(combinedData)
                    }
                    else -> {
                        Resource.Error(errorMessages.joinToString("; "))
                    }
                }

            } catch (e: IOException) {
                _uiStateListEventChooseDay.value = Resource.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }


}