package com.charlezz.mvi.ui

import com.charlezz.mvi.ui.base.Action

sealed class PhotoAction: Action {

    object Initialize: PhotoAction()

    data class SearchKeyword(
        val keyword: String
    ): PhotoAction()

}
