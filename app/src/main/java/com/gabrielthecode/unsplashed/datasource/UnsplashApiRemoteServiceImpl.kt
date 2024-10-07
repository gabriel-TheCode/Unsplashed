package com.gabrielthecode.unsplashed.datasource

import com.gabrielthecode.unsplashed.datasource.network.api.UnsplashApi
import com.gabrielthecode.unsplashed.datasource.network.model.SearchResponse
import okhttp3.ResponseBody
import retrofit2.Response

class UnsplashApiRemoteServiceImpl(
    private val unsplashApi: UnsplashApi
) : UnsplashApiRemoteService {

    override suspend fun searchPhotos(
        query: String
    ): SearchResponse {
        return unsplashApi.searchPhotos(query)
    }

    override suspend fun downloadPhoto(
        url: String
    ): Response<ResponseBody> {
        return unsplashApi.downloadPhoto(url)
    }
}
