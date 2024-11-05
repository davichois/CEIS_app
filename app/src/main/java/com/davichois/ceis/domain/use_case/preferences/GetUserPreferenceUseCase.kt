package com.davichois.ceis.domain.use_case.preferences

import com.davichois.ceis.domain.repository.PreferencesRepository
import javax.inject.Inject

class GetUserPreferenceUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {

    suspend operator fun invoke() =
        repository.getUserPreferences()

}