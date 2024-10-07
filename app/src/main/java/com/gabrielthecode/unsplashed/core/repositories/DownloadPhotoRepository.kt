package com.gabrielthecode.unsplashed.core.repositories

import com.gabrielthecode.unsplashed.core.domain.DownloadPhotoDomainModel
import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.core.remote.RemoteDataSourceImpl
import javax.inject.Inject

class DownloadPhotoRepository @Inject constructor(
	private val networkDataSource: RemoteDataSourceImpl
) {
	suspend fun downloadPhoto(
		url: String
	): DownloadPhotoDomainModel {
		return networkDataSource.downloadPhoto(url)
	}
}
