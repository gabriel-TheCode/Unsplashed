package com.gabrielthecode.unsplashed.core.repositories

import com.gabrielthecode.unsplashed.core.local.LocalDataSourceImpl
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val localDataSource: LocalDataSourceImpl
) {

    suspend fun clearAppData() {
        localDataSource.clearAppData()
    }
}
