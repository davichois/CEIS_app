package com.davichois.ceis.data.repository

import com.davichois.ceis.data.preferences.PreferencesImplement
import com.davichois.ceis.data.preferences.dto.UserPreference
import com.davichois.ceis.domain.repository.PreferencesRepository

class PreferencesRepositoryImplementation(
    private val api: PreferencesImplement
): PreferencesRepository {

    override suspend fun putUserPreferences(userPreference: UserPreference) {
        return api.putUserPreferences(userPreferencesApp = userPreference)
    }

    override suspend fun getUserPreferences(): UserPreference =
        api.getUserPreferences()

    override suspend fun deleteUserPreferences() {
        return api.deleteUserPreferences()
    }

}