package com.gabrielthecode.unsplashed.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielthecode.unsplashed.core.domain.Resource
import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.core.usecases.SearchPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPhotosUseCase: SearchPhotos
) : ViewModel() {
    private val _searchState = MutableLiveData<Resource<SearchDomainModel>>()
    val searchState: LiveData<Resource<SearchDomainModel>>
        get() = _searchState

    fun searchPhotos(query: String) {
        viewModelScope.launch {
            _searchState.value.let { _ ->
                searchPhotosUseCase.searchPhotos(query).onEach {
                    _searchState.value = it
                }.launchIn(viewModelScope)
            }
        }
    }
}
