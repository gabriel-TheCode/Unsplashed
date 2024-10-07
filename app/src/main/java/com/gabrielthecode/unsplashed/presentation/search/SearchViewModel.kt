package com.gabrielthecode.unsplashed.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielthecode.unsplashed.core.domain.Resource
import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.core.usecases.SearchPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val searchPhotos: SearchPhotos
) : ViewModel() {
	private val _searchState = MutableLiveData<Resource<SearchDomainModel>>()
	val searchState: LiveData<Resource<SearchDomainModel>>
		get() = _searchState

	fun searchPhotos(query: String) {
		viewModelScope.launch {
			searchPhotos.invoke(query).collect {
				_searchState.value = it
			}
		}
	}
}
