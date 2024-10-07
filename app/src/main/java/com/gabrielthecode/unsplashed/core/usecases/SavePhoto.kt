package com.gabrielthecode.unsplashed.core.usecases

import android.graphics.Bitmap
import android.net.Uri
import com.gabrielthecode.unsplashed.core.domain.Resource
import com.gabrielthecode.unsplashed.core.repositories.SavePhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SavePhoto @Inject constructor(
	private val repository: SavePhotoRepository
) {
	operator fun invoke(bitmap: Bitmap): Flow<Resource<Uri>> = flow {
		emit(Resource.loading())
		try {
			val uri = repository.savePhoto(bitmap)
			emit(Resource.success(uri))
		} catch (e: Exception) {
			emit(Resource.failure(e))
		}
	}
}