package com.gabrielthecode.unsplashed.core.usecases

import com.gabrielthecode.unsplashed.core.domain.DownloadPhotoDomainModel
import com.gabrielthecode.unsplashed.core.domain.Resource
import com.gabrielthecode.unsplashed.core.repositories.DownloadPhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DownloadPhoto @Inject constructor(
	private val repository: DownloadPhotoRepository
) {
	operator fun invoke(
		query: String
	): Flow<Resource<DownloadPhotoDomainModel>> = flow {
		emit(Resource.loading())
		try {
			val data = repository.downloadPhoto(query)
			emit(Resource.Success(data))
		} catch (e: Exception) {
			emit(Resource.Failure(e))
		}
	}.flowOn(Dispatchers.IO)
}
