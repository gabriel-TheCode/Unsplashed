package com.gabrielthecode.unsplashed.application

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gabrielthecode.unsplashed.application.UnsplashedDataStore.Companion.PREFERENCE_NAME
import com.gabrielthecode.unsplashed.utils.extensions.getValueFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCE_NAME)

class UnsplashedDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    suspend fun setTokenAccess(token: String) {
        val dataStoreKey = stringPreferencesKey(TOKEN_ACCESS)
        dataStore.edit { preferences ->
            preferences[dataStoreKey] = token
        }
    }

    fun getTokenAccess(): Flow<String> {
        val dataStoreKey = stringPreferencesKey(TOKEN_ACCESS)
        return dataStore.getValueFlow(dataStoreKey, "")
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        var PREFERENCE_NAME = "com.thecode.myblablacar"
        private const val TOKEN_ACCESS = "TOKEN_ACCESS"
    }
}
