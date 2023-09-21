package com.gabrielthecode.unsplashed.core.local

import com.gabrielthecode.unsplashed.application.UnsplashedDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalDataSource {
    suspend fun clearAppData()
}

class LocalDataSourceImpl @Inject constructor(
    private val dataStore: UnsplashedDataStore
) : LocalDataSource {

    override suspend fun clearAppData() {
        dataStore.clearSession()
    }
}
