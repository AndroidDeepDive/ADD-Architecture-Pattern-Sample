package com.charlezz.mvi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
            is PhotoAction.Initialize -> {
                viewModelScope.launch {
                    setState(action) {
                        copy(
                            isLoading = true
                        )
                    }
                    searchUseCase("")
                        .map { pagingData -> pagingData.map { PhotoUiModel(it) } }
                        .collectLatest { photoData ->
                            setState(action) {
                                copy(
                                    photoData = photoData,
                                    isLoading = false
                                )
                            }
                        }
                }
            }
            is PhotoAction.SearchKeyword -> {
                viewModelScope.launch {
                    setState(action) {
                        copy(
                            isLoading = true
                        )
                    }
                    searchUseCase(action.keyword)
                        .map { pagingData -> pagingData.map { PhotoUiModel(it) } }
                        .collectLatest { photoData ->
                            setState(action) {
                                copy(
                                    photoData = photoData,
                                    isLoading = false
                                )
                            }
                        }
                }
            }
        }
    }

    @AssistedFactory
    interface PhotoViewModelFactory {

        fun create(state: PhotoState): PhotoViewModel

    }

    class Factory(
        private val assistedFactory: PhotoViewModelFactory,
        private val state: PhotoState
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
