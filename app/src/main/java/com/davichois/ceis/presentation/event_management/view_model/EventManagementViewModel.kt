package com.davichois.ceis.presentation.event_management.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.domain.use_case.event.GetEventForCodeUseCase
import com.davichois.ceis.domain.use_case.event.GetEventForDayUseCase
import com.davichois.ceis.domain.use_case.user.GetRecordersUserControlledUseCase
import com.davichois.ceis.domain.use_case.user.GetRecordersUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EventManagementViewModel @Inject constructor(
    private val getEventForCodeUseCase: GetEventForCodeUseCase,
    private val getEventForDayUseCase: GetEventForDayUseCase,
    private val getRecordersUserControlledUseCase: GetRecordersUserControlledUseCase
): ViewModel() {

    private val _uiStateEventForCode = MutableStateFlow<Resource<EventDTO>>(Resource.PreLoad)
    val uiStateEventForCode: StateFlow<Resource<EventDTO>> = _uiStateEventForCode

    private val _uiStateListEventChooseDay = MutableStateFlow<Resource<List<EventModel>>>(Resource.PreLoad)
    val uiStateListEventChooseDay: StateFlow<Resource<List<EventModel>>> = _uiStateListEventChooseDay

    private val _uiStateListEventChooseAndGeneralDay = MutableStateFlow<Resource<List<EventModel>>>(Resource.PreLoad)
    val uiStateListEventChooseAndGeneralDay: StateFlow<Resource<List<EventModel>>> = _uiStateListEventChooseAndGeneralDay

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
            delay(2000)

            try {
                val resultEventSPDeferred = async { getEventForDayUseCase(day = day, type = "SESIONES_PARALELAS") }
                val resultEventColoquioDeferred = async { getEventForDayUseCase(day = day, type = "COLOQUIO") }
                val resultEventBookingDeferred = async { getRecordersUserControlledUseCase(day = day) }

                val resultEventSP = resultEventSPDeferred.await()
                val resultEventColoquio = resultEventColoquioDeferred.await()
                val resultEventBooking = resultEventBookingDeferred.await()

                // Variables para combinar los resultados exitosos y capturar errores
                val combinedData = mutableListOf<EventModel>()
                val errorMessages = mutableListOf<String>()
                val dataBooking = mutableListOf<EventModel>()

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

                if (resultEventBooking is Resource.Success) {
                    dataBooking += resultEventBooking.data
                } else if (resultEventBooking is Resource.Error) {
                    errorMessages.add("Error en RECORDERS_USER_CONTROLLED: ${resultEventBooking.message}")
                }

                // Obtener IDs de los eventos duplicados en ambas listas
                val duplicateIds = combinedData.map { it.id }.intersect(dataBooking.map { it.id })

                // Filtrar para remover eventos que están en ambas listas
                val combinedDataNotDuplicated = (combinedData + dataBooking).filterNot { it.id in duplicateIds }


                // Decide el estado final en función de los resultados
                _uiStateListEventChooseDay.value = when {
                    combinedDataNotDuplicated.isNotEmpty() -> {
                        if (errorMessages.isNotEmpty()) {
                            Resource.Success(combinedDataNotDuplicated).apply {
                                errorMessages.forEach { message -> println("Warning: $message") }
                            }
                        } else {
                            Resource.Success(combinedDataNotDuplicated)
                        }
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


    fun getEventForChooseAndGeneralDay(day: String) {
        viewModelScope.launch {
            _uiStateListEventChooseAndGeneralDay.value = Resource.Loading
            delay(2000)

            try {
                val resultEventCMDeferred = async { getEventForDayUseCase(day = day, type = "CHARLA_MAGISTRAL") }
                val resultEventGeneralDeferred = async { getEventForDayUseCase(day = day, type = "GENERAL") }
                val resultEventOtherDeferred = async { getEventForDayUseCase(day = day, type = "OTROS") }
                val resultEventBookingDeferred = async { getRecordersUserControlledUseCase(day = day) }

                val resultEventCM = resultEventCMDeferred.await()
                val resultEventGeneral = resultEventGeneralDeferred.await()
                val resultEventOther = resultEventOtherDeferred.await()
                val resultEventBooking = resultEventBookingDeferred.await()

                val combinedData = mutableListOf<EventModel>()
                val errorMessages = mutableListOf<String>()

                // Procesa cada resultado
                if (resultEventCM is Resource.Success) {
                    combinedData += resultEventCM.data
                } else if (resultEventCM is Resource.Error) {
                    errorMessages.add("Error en SESIONES_PARALELAS: ${resultEventCM.message}")
                }

                if (resultEventGeneral is Resource.Success) {
                    combinedData += resultEventGeneral.data
                } else if (resultEventGeneral is Resource.Error) {
                    errorMessages.add("Error en COLOQUIO: ${resultEventGeneral.message}")
                }

                if (resultEventOther is Resource.Success) {
                    combinedData += resultEventOther.data
                } else if (resultEventOther is Resource.Error) {
                    errorMessages.add("Error en COLOQUIO: ${resultEventOther.message}")
                }

                if (resultEventOther is Resource.Success) {
                    combinedData += resultEventOther.data
                } else if (resultEventOther is Resource.Error) {
                    errorMessages.add("Error en COLOQUIO: ${resultEventOther.message}")
                }

                if (resultEventBooking is Resource.Success) {
                    combinedData += resultEventBooking.data
                } else if (resultEventBooking is Resource.Error) {
                    errorMessages.add("Error en COLOQUIO: ${resultEventBooking.message}")
                }

                // Decide el estado final en función de los resultados
                _uiStateListEventChooseAndGeneralDay.value = when {
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
                _uiStateListEventChooseAndGeneralDay.value = Resource.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

}