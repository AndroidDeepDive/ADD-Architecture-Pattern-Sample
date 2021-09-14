package com.charlezz.mvi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.charlezz.domain.usecase.SearchUseCase
import com.charlezz.mvi.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PhotoViewModel @AssistedInject constructor(
    @Assisted state: PhotoState,
    private val searchUseCase: SearchUseCase,
) : BaseViewModel<PhotoAction, PhotoState>(state) {

    override fun handleAction(action: PhotoAction) {
        when (action) {
            is PhotoAction.Initialize -> handleInitialize()
            is PhotoAction.SearchKeyword -> handleSearchKeyword(action)
        }
    }

    private fun handleInitialize() = viewModelScope.launch {
        searchUseCase("")
            .map { pagingData -> pagingData.map { PhotoUiModel(it) } }
            .cachedIn(viewModelScope)
            .collectLatest { photoData ->
                setState {
                    PhotoState.List(photoData = photoData)
                }
            }
    }

    private fun handleSearchKeyword(action: PhotoAction.SearchKeyword) = viewModelScope.launch {
        searchUseCase(action.keyword)
            .map { pagingData -> pagingData.map { PhotoUiModel(it) } }
            .cachedIn(viewModelScope)
            .collectLatest { photoData ->
                setState {
                    PhotoState.List(photoData = photoData)
                }
            }
    }

    @AssistedFactory
    interface PhotoViewModelFactory {

        fun create(state: PhotoState): PhotoViewModel

    }

    class Factory(
        private val assistedFactory: PhotoViewModelFactory,
        private val state: PhotoState = PhotoState.Unitialized
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
                assistedFactory.create(state) as T
            } else {
                throw IllegalArgumentException()
            }
        }

    }

}
