package com.gabrielthecode.unsplashed.presentation.search.viewholders

import android.view.View

interface PhotoOnClickListener {

    fun openPhoto(photo: PhotoUiModel)

    fun showImageFullResolution(photo: PhotoUiModel, view: View)
}