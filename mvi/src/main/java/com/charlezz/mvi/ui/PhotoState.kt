package com.charlezz.mvi.ui

import androidx.paging.PagingData
import com.charlezz.mvi.ui.base.State

sealed class PhotoState : State {

    object Unitialized : PhotoState()

    data class List(
        val photoData: PagingData<PhotoUiModel>
    ) : PhotoState()

}
