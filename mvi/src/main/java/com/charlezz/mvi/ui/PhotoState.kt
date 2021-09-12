package com.charlezz.mvi.ui

import androidx.paging.PagingData
import com.charlezz.mvi.ui.base.State

data class PhotoState(
    val isLoading: Boolean,
    val photoData: PagingData<PhotoUiModel>
) : State {

    companion object {
        fun initialize(): PhotoState =
            PhotoState(false, PagingData.empty())
    }

}
