package com.gabrielthecode.unsplashed.application

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.gabrielthecode.unsplashed.application.UnsplashedDataStore.Companion.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCE_NAME)

class UnsplashedDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        var PREFERENCE_NAME = "com.thecode.myblablacar"
    }
}
