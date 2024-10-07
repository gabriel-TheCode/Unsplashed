package com.gabrielthecode.unsplashed.core.remote

import com.gabrielthecode.unsplashed.core.domain.DownloadPhotoDomainModel
import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.datasource.UnsplashApiRemoteService
import com.gabrielthecode.unsplashed.datasource.network.mapper.SearchMapper
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun searchPhotos(
        query: String
    ): SearchDomainModel

    suspend fun downloadPhoto(
        url: String
    ): DownloadPhotoDomainModel
}