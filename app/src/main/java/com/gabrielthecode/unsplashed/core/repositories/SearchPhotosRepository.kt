package com.gabrielthecode.unsplashed.core.repositories

import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.core.remote.RemoteDataSourceImpl
import javax.inject.Inject

class SearchPhotosRepository @Inject constructor(
	private val networkDataSource: RemoteDataSourceImpl
) {
	suspend fun searchPhotos(
		query: String
	): SearchDomainModel {
		return networkDataSource.searchPhotos(query)
	}
}


