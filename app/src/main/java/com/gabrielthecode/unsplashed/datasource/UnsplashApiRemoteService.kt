package com.gabrielthecode.unsplashed.datasource

import com.gabrielthecode.unsplashed.datasource.network.model.SearchResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface UnsplashApiRemoteService {
	suspend fun searchPhotos(
		query: String
	): SearchResponse

	suspend fun downloadPhoto(
		url: String
	): Response<ResponseBody>
}