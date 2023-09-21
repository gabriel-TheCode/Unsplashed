package com.gabrielthecode.unsplashed.core.remote

import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.datasource.UnsplashApiRemoteService
import com.gabrielthecode.unsplashed.datasource.network.mapper.SearchMapper
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun searchPhotos(
        query: String
    ): SearchDomainModel
}

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: UnsplashApiRemoteService,
    private val searchMapper: SearchMapper,
) : RemoteDataSource {

    override suspend fun searchPhotos(
        query: String
    ): SearchDomainModel {
        return searchMapper.mapToDomain(
            apiService.searchPhotos(
                query
            )
        )
    }
}
