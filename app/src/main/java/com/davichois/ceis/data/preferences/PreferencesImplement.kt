package com.davichois.ceis.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
        val dia12 = booleanPreferencesKey("dia12")
        val dia13 = booleanPreferencesKey("dia13")
        val dia14 = booleanPreferencesKey("dia14")
        val dia15 = booleanPreferencesKey("dia15")
    }

    suspend fun putUserPreferences(userPreferencesApp: UserPreference) {
        context.dataStore.edit { userPreference ->
            userPreference[dni] = userPreferencesApp.dni
        }
    }

    suspend fun putDayPreferences(userPreferencesApp: UserPreference) {
        context.dataStore.edit { userPreference ->
            userPreference[dia12] = userPreferencesApp.dia12
            userPreference[dia13] = userPreferencesApp.dia13
            userPreference[dia14] = userPreferencesApp.dia14
            userPreference[dia15] = userPreferencesApp.dia15
        }
    }

    suspend fun getUserPreferences() =
        context.dataStore.data.map { userPreference ->
            UserPreference(
                dni = userPreference[dni] ?: "",
                dia12 = userPreference[dia12] ?: false,
                dia13 = userPreference[dia13] ?: false,
                dia14 = userPreference[dia14] ?: false,
                dia15 = userPreference[dia15] ?: false,
                )
        }.first()

    suspend fun deleteUserPreferences() {
        context.dataStore.edit { userPreference ->
            userPreference.clear()
        }
    }

}