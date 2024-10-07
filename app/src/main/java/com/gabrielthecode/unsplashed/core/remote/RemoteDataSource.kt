package com.gabrielthecode.unsplashed.core.remote

import com.gabrielthecode.unsplashed.core.domain.DownloadPhotoDomainModel
import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel

interface RemoteDataSource {
	suspend fun searchPhotos(
		query: String
	): SearchDomainModel

	suspend fun downloadPhoto(
		url: String
	): DownloadPhotoDomainModel
}