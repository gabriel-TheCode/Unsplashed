package com.gabrielthecode.unsplashed.core.usecases

import com.gabrielthecode.unsplashed.core.domain.Resource
import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.core.repositories.SearchPhotosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchPhotos @Inject constructor(
	private val repository: SearchPhotosRepository
) {
	operator fun invoke(
		query: String
	): Flow<Resource<SearchDomainModel>> = flow {
		emit(Resource.loading())
		try {
			val data = repository.searchPhotos(query)
			if (data.results?.isEmpty() == true) {
				emit(Resource.Failure(Exception("No result found")))
			} else {
				emit(Resource.Success(data))
			}
		} catch (e: Exception) {
			emit(Resource.Failure(e))
		}
	}.flowOn(Dispatchers.IO)
}
