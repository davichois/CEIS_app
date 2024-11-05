package com.davichois.ceis.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.davichois.ceis.data.preferences.dto.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PREFERENCES_NAME = "ceis_preferences"

val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

class PreferencesImplement @Inject constructor(
    private val context: Context
) {

    companion object {
        val dni = stringPreferencesKey("dni")
    }

    suspend fun putUserPreferences(userPreferencesApp: UserPreference) {
        context.dataStore.edit { userPreference ->
            userPreference[dni] = userPreferencesApp.dni
        }
    }

    suspend fun getUserPreferences() =
        context.dataStore.data.map { userPreference ->
            UserPreference(
                dni = userPreference[dni] ?: "",
            )
        }.first()

    suspend fun deleteUserPreferences() {
        context.dataStore.edit { userPreference ->
            userPreference.clear()
        }
    }

}