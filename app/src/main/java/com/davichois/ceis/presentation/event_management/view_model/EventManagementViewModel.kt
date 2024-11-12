package com.davichois.ceis.presentation.event_management.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.remote.dto.EventDTO
import com.davichois.ceis.domain.model.EventModel
import com.davichois.ceis.domain.use_case.event.GetEventForCodeUseCase
import com.davichois.ceis.domain.use_case.event.GetEventForDayMasterUseCase
import com.davichois.ceis.domain.use_case.event.GetEventForDayUseCase
import com.davichois.ceis.domain.use_case.event.PostBookingRecorderUseCase
import com.davichois.ceis.domain.use_case.preferences.DeleteUserPreferenceUseCase
import com.davichois.ceis.domain.use_case.preferences.GetUserPreferenceUseCase
import com.davichois.ceis.domain.use_case.user.GetRecordersUserControlledUseCase
import com.davichois.ceis.domain.use_case.user.GetRecordersUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EventManagementViewModel @Inject constructor(
    private val getEventForCodeUseCase: GetEventForCodeUseCase,
    private val getEventForDayUseCase: GetEventForDayUseCase,
    private val getRecordersUserControlledUseCase: GetRecordersUserControlledUseCase,
    private val getUserPreferenceUseCase: GetUserPreferenceUseCase,
    private val deleteUserPreferenceUseCase: DeleteUserPreferenceUseCase,
    private val getEventForDayMasterUseCase: GetEventForDayMasterUseCase,
    private val postBookingRecorderUseCase: PostBookingRecorderUseCase
): ViewModel() {

    private val _uiStateEventForCode = MutableStateFlow<Resource<EventDTO>>(Resource.PreLoad)
    val uiStateEventForCode: StateFlow<Resource<EventDTO>> = _uiStateEventForCode

    private val _uiStateListEventChooseDay = MutableStateFlow<Resource<List<EventModel>>>(Resource.PreLoad)
    val uiStateListEventChooseDay: StateFlow<Resource<List<EventModel>>> = _uiStateListEventChooseDay

    private val _uiStateListEventChooseAndGeneralDay = MutableStateFlow<Resource<List<EventModel>>>(Resource.PreLoad)
    val uiStateListEventChooseAndGeneralDay: StateFlow<Resource<List<EventModel>>> = _uiStateListEventChooseAndGeneralDay

    private val _uiStateRoleUser = MutableStateFlow<Resource<String>>(Resource.PreLoad)
    val uiStateRoleUser: StateFlow<Resource<String>> = _uiStateRoleUser

    private val _uiStateEventReturn = MutableStateFlow<Resource<String>>(Resource.PreLoad)
    val uiStateEventListReturn: StateFlow<Resource<String>> = _uiStateEventReturn

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
                when (day) {
                    "12" -> {
                        val combinedData = mutableListOf<EventModel>()
                        val errorMessages = mutableListOf<String>()
                        val dataBooking = mutableListOf<EventModel>()

                        val jobColoquio = launch {
                            val resultEventColoquio = getEventForDayUseCase(day = day, type = "COLOQUIO")
                            if (resultEventColoquio is Resource.Success) {
                                combinedData += resultEventColoquio.data
                            } else if (resultEventColoquio is Resource.Error) {
                                errorMessages.add("Error en COLOQUIO: ${resultEventColoquio.message}")
                            }
                        }

                        val jobBooking = launch {
                            val resultEventBooking = getRecordersUserControlledUseCase(day = day)
                            if (resultEventBooking is Resource.Success) {
                                dataBooking += resultEventBooking.data
                            } else if (resultEventBooking is Resource.Error) {
                                errorMessages.add("Error en RECORDERS_USER_CONTROLLED: ${resultEventBooking.message}")
                            }
                        }

                        // Esperamos que ambos jobs finalicen
                        joinAll(jobColoquio, jobBooking)

                        // Filtrar eventos duplicados
                        val duplicateIds = combinedData.map { it.id }.intersect(dataBooking.map { it.id })
                        val combinedDataNotDuplicated = (combinedData + dataBooking).filterNot { it.id in duplicateIds }

                        // Asignar el resultado al estado final
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
                    }

                    "13", "14" -> {
                        val combinedData = mutableListOf<EventModel>()
                        val errorMessages = mutableListOf<String>()
                        val dataBooking = mutableListOf<EventModel>()

                        val jobSP = launch {
                            val resultEventSP = getEventForDayUseCase(day = day, type = "SESIONES_PARALELAS")
                            if (resultEventSP is Resource.Success) {
                                combinedData += resultEventSP.data
                            } else if (resultEventSP is Resource.Error) {
                                errorMessages.add("Error en SESIONES_PARALELAS: ${resultEventSP.message}")
                            }
                        }

                        val jobBooking = launch {
                            val resultEventBooking = getRecordersUserControlledUseCase(day = day)
                            if (resultEventBooking is Resource.Success) {
                                dataBooking += resultEventBooking.data
                            } else if (resultEventBooking is Resource.Error) {
                                errorMessages.add("Error en RECORDERS_USER_CONTROLLED: ${resultEventBooking.message}")
                            }
                        }

                        // Esperamos que ambos jobs finalicen
                        joinAll(jobSP, jobBooking)

                        // Filtrar eventos duplicados
                        val duplicateIds = combinedData.map { it.id }.intersect(dataBooking.map { it.id })
                        val combinedDataNotDuplicated = (combinedData + dataBooking).filterNot { it.id in duplicateIds }

                        // Asignar el resultado al estado final
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
                when (getUserPreferenceUseCase().role){
                    "STAFF" -> {
                        when(val result = getEventForDayMasterUseCase(day)) {
                            is Resource.Success -> {
                                _uiStateListEventChooseAndGeneralDay.value = Resource.Success(result.data)
                            }
                            is Resource.Error -> {
                                _uiStateListEventChooseAndGeneralDay.value = Resource.Error("Error en master")
                            }

                            else -> {
                                _uiStateListEventChooseAndGeneralDay.value = Resource.Loading
                            }
                        }
                    }
                    "STUDENT" -> {
                        val combinedData = mutableListOf<EventModel>()
                        val errorMessages = mutableListOf<String>()

                        val jobCM = launch {
                            val resultEventCM = getEventForDayUseCase(day = day, type = "CHARLA_MAGISTRAL")
                            if (resultEventCM is Resource.Success) {
                                combinedData += resultEventCM.data
                            } else if (resultEventCM is Resource.Error) {
                                errorMessages.add("Error en SESIONES_PARALELAS: ${resultEventCM.message}")
                            }
                        }

                        val jobGeneral = launch {
                            val resultEventGeneral = getEventForDayUseCase(day = day, type = "GENERAL")
                            if (resultEventGeneral is Resource.Success) {
                                combinedData += resultEventGeneral.data
                            } else if (resultEventGeneral is Resource.Error) {
                                errorMessages.add("Error en COLOQUIO: ${resultEventGeneral.message}")
                            }
                        }

                        val jobBooking = launch {
                            val resultEventBooking = getRecordersUserControlledUseCase(day = day)
                            if (resultEventBooking is Resource.Success) {
                                combinedData += resultEventBooking.data
                            } else if (resultEventBooking is Resource.Error) {
                                errorMessages.add("Error en COLOQUIO: ${resultEventBooking.message}")
                            }
                        }

                        joinAll(jobCM, jobGeneral, jobBooking)

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

                    }
                }

            } catch (e: IOException) {
                _uiStateListEventChooseAndGeneralDay.value = Resource.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

    fun postAllBookings(events: List<EventModel>) {
        viewModelScope.launch {
            _uiStateEventReturn.value = Resource.Loading
            try {
                when(val result = postBookingRecorderUseCase(event = events)) {
                    is Resource.Success -> {
                        _uiStateEventReturn.value = Resource.Success(data = result.data)
                    }
                    is Resource.Error -> {
                        _uiStateEventReturn.value = Resource.Error("Error event for code")
                    }

                    else -> {
                        _uiStateEventReturn.value = Resource.Loading
                    }
                }
            } catch (e: IOException) {
                _uiStateEventReturn.value = Resource.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

    fun verifiedRoleUser(){
        viewModelScope.launch {
            _uiStateRoleUser.value = Resource.Loading
            val result = getUserPreferenceUseCase()
            _uiStateRoleUser.value = Resource.Success(data = result.role)
        }
    }

    fun logOutUserApp(){
        viewModelScope.launch {
            deleteUserPreferenceUseCase()
        }
    }

}