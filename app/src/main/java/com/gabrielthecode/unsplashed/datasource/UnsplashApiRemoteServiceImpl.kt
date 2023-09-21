package com.gabrielthecode.unsplashed.datasource

import com.gabrielthecode.unsplashed.datasource.network.api.UnsplashApi
import com.gabrielthecode.unsplashed.datasource.network.model.SearchResponse

interface UnsplashApiRemoteService {
    suspend fun searchPhotos(
        query: String
    ): SearchResponse
}

class UnsplashApiRemoteServiceImpl constructor(
    private val unsplashApi: UnsplashApi
) : UnsplashApiRemoteService {

    override suspend fun searchPhotos(
        query: String
    ): SearchResponse {
        return unsplashApi.searchPhotos(query)
    }
}
