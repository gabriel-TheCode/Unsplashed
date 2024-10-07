package com.gabrielthecode.unsplashed.core.remote

import android.graphics.BitmapFactory
import com.gabrielthecode.unsplashed.core.domain.DownloadPhotoDomainModel
import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.datasource.UnsplashApiRemoteService
import com.gabrielthecode.unsplashed.datasource.network.mapper.SearchMapper
import javax.inject.Inject

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

	override suspend fun downloadPhoto(
		url: String
	): DownloadPhotoDomainModel {
		val response = apiService.downloadPhoto(url)
		if (response.isSuccessful) {
			response.body()?.let { responseBody ->
				val inputStream = responseBody.byteStream()
				val bitmap = BitmapFactory.decodeStream(inputStream)
				return DownloadPhotoDomainModel(bitmap)
			}
		}
		throw Exception("Failed to download image")
	}
}
