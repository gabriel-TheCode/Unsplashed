package com.gabrielthecode.unsplashed.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielthecode.unsplashed.core.domain.DownloadPhotoDomainModel
import com.gabrielthecode.unsplashed.core.domain.Resource
import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.core.usecases.DownloadPhoto
import com.gabrielthecode.unsplashed.core.usecases.SearchPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
	private val downloadPhoto: DownloadPhoto
) : ViewModel() {
	private val _downloadState = MutableLiveData<Resource<DownloadPhotoDomainModel>>()
	val downloadState: LiveData<Resource<DownloadPhotoDomainModel>>
		get() = _downloadState

	fun downloadPhoto(url: String) {
		viewModelScope.launch {
			downloadPhoto.invoke(url).collect {
				_downloadState.value = it
			}
		}
	}
}