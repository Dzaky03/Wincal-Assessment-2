package com.dzaky3022.asesment1.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "data_prefs")

class DataStore(private val context: Context) {
    private val EMAIL_KEY = stringPreferencesKey("email_key")
    private val IS_LIST = booleanPreferencesKey("is_list")

    val emailFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[EMAIL_KEY] ?: ""
    }

    val layoutFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LIST] ?: true
    }

    suspend fun saveLayout(isList: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LIST] = isList
        }
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun clearEmail() {
        context.dataStore.edit { it.remove(EMAIL_KEY) }
    }
}