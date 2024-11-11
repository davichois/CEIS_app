package com.davichois.ceis.presentation.login_management.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davichois.ceis.core.common.Resource
import com.davichois.ceis.data.preferences.dto.UserPreference
import com.davichois.ceis.data.remote.dto.AuthUserDTO
import com.davichois.ceis.domain.use_case.auth.PostAuthenticateUseCase
import com.davichois.ceis.domain.use_case.preferences.GetUserPreferenceUseCase
import com.davichois.ceis.domain.use_case.preferences.PostUserPreferenceUseCase
import com.davichois.ceis.domain.use_case.user.GetRecordersUserUseCase
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
    private val getUserPreferenceUseCase: GetUserPreferenceUseCase,
    private val getRecordersUserUseCase: GetRecordersUserUseCase,
    private val postUserPreferenceUseCase: PostUserPreferenceUseCase,
): ViewModel() {

    private val _uiStateLogin = MutableStateFlow<Resource<AuthUserDTO>>(Resource.PreLoad)
    val uiStateLogin: StateFlow<Resource<AuthUserDTO>> = _uiStateLogin

    private val _uiStateNavigate = MutableStateFlow<Resource<Int>>(Resource.PreLoad)
    val uiStateNavigate: StateFlow<Resource<Int>> = _uiStateNavigate

    private val _uiStateNavigateSplash = MutableStateFlow<Resource<Int>>(Resource.PreLoad)
    val uiStateNavigateSplash: StateFlow<Resource<Int>> = _uiStateNavigateSplash

    fun authenticateUser(dni: String, day: String) {
        viewModelScope.launch {
            _uiStateLogin.value = Resource.Loading
            try {
                when(val result = postAuthenticateUseCase(dni = dni)) {
                    is Resource.Success -> {
                        _uiStateLogin.value = Resource.Success(data = result.data)
                        postUserPreferenceUseCase(UserPreference(dni = dni, dia12 = false, dia13 = false, dia14 = false,
                            dia15 = false
                        ))
                        _uiStateNavigate.value = Resource.Success(data = run {
                            val numRecords = getRecordersUserUseCase(day, dni = dni)

                            fun getValueBasedOnRecordCount(minRecords: Int): Int {
                                return if (numRecords.size >= minRecords) 2 else 1
                            }

                            when(day) {
                                "12" -> getValueBasedOnRecordCount(4)
                                "13", "14" -> getValueBasedOnRecordCount(2)
                                "15" -> 2
                                else -> 2
                            }
                        })
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

    fun getVerifiedAccountInAppInitialized(day: String) {
        viewModelScope.launch {
            _uiStateNavigateSplash.value = Resource.Loading
            try {
                val dni = getUserPreferenceUseCase().dni
                _uiStateNavigateSplash.value = Resource.Success(data = if (dni.isNotBlank()) {
                    val numRecords = getRecordersUserUseCase(day)

                    fun getValueBasedOnRecordCount(minRecords: Int): Int {
                        return if (numRecords.size >= minRecords) 3 else 1
                    }

                    when(day) {
                        "12" -> getValueBasedOnRecordCount(4)
                        "13", "14" -> getValueBasedOnRecordCount(2)  // Para 13 y 14, lógica es la misma
                        "15" -> 3  // Para el día 15, siempre es 1
                        else -> 3  // Para otros días, puedes poner un valor por defecto
                    }
                } else {
                    2
                })
            } catch (e: IOException) {
                _uiStateNavigateSplash.value = Resource.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

}