package com.gabrielthecode.unsplashed.datasource.network.api

import com.gabrielthecode.unsplashed.datasource.network.model.SearchResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface UnsplashApi {
	@Headers("Accept-Version: v1")
	@GET("search/photos")
	suspend fun searchPhotos(
		@Query("query") query: String?,
		@Query("per_page") pageSize: Int = 30
	): SearchResponse

	@GET
	suspend fun downloadPhoto(@Url url: String): Response<ResponseBody>
}
