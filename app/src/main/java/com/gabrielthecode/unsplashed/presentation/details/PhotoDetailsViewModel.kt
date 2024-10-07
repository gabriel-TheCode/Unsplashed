package com.gabrielthecode.unsplashed.presentation.details

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielthecode.unsplashed.core.domain.DownloadPhotoDomainModel
import com.gabrielthecode.unsplashed.core.domain.Resource
import com.gabrielthecode.unsplashed.core.usecases.DownloadPhoto
import com.gabrielthecode.unsplashed.core.usecases.SavePhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
	private val downloadPhoto: DownloadPhoto,
	private val savePhoto: SavePhoto
) : ViewModel() {
	private val _downloadState = MutableLiveData<Resource<DownloadPhotoDomainModel>>()
	val downloadState: LiveData<Resource<DownloadPhotoDomainModel>> get() = _downloadState

	private val _saveState = MutableLiveData<Resource<Uri>>()
	val saveState: LiveData<Resource<Uri>> get() = _saveState

	fun downloadPhoto(url: String) {
		viewModelScope.launch {
			downloadPhoto.invoke(url).collect {
				_downloadState.value = it
			}
		}
	}

	fun savePhoto(bitmap: Bitmap) {
		viewModelScope.launch {
			savePhoto.invoke(bitmap).collect {
				_saveState.value = it
			}
		}
	}
}

data class DownloadResult(val imageUri: Uri)