package com.davichois.ceis.domain.repository

import com.davichois.ceis.data.preferences.dto.UserPreference

interface PreferencesRepository {

    suspend fun putUserPreferences(userPreference: UserPreference)

    suspend fun getUserPreferences(): UserPreference

    suspend fun deleteUserPreferences()

}