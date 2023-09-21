package com.gabrielthecode.unsplashed.core.local

import com.gabrielthecode.unsplashed.application.UnsplashedDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalDataSource {

    fun getSavedToken(): Flow<String>

    suspend fun setTokenAccess(token: String)

    suspend fun clearAppData()
}

class LocalDataSourceImpl @Inject constructor(
    private val dataStore: UnsplashedDataStore
) : LocalDataSource {

    override fun getSavedToken(): Flow<String> {
        return dataStore.getTokenAccess()
    }

    override suspend fun setTokenAccess(token: String) {
        dataStore.setTokenAccess(token)
    }

    override suspend fun clearAppData() {
        dataStore.clearSession()
    }
}
