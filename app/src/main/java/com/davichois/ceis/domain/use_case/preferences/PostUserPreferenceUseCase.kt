package com.davichois.ceis.domain.use_case.preferences

import com.davichois.ceis.data.preferences.dto.UserPreference
import com.davichois.ceis.domain.repository.PreferencesRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class PostUserPreferenceUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {

    suspend operator fun invoke(userPreference: UserPreference) {
        repository.putUserPreferences(userPreference = userPreference)
        delay(1000)
    }

}