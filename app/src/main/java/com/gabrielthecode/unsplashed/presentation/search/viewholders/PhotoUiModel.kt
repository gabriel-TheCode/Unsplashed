package com.gabrielthecode.unsplashed.presentation.search.viewholders

import android.os.Parcelable
import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoUiModel(
    val width: Int,
    val height: Int,
    val url: String,
    val thumbnail: String,
    val likes: Int,
    val dominantColor: String,
    val authorName: String,
    val description: String,
    val creationDate: String
) : Parcelable

fun SearchDomainModel.toUiModels(): List<PhotoUiModel> {
    return results?.map {
        PhotoUiModel(
            width = it.width,
            height = it.height,
            url = it.urls.full,
            thumbnail = it.urls.thumb,
            likes = it.likes ?: 0,
            dominantColor = it.color ?: "N/A",
            description = it.description ?: "Description unavailable",
            authorName = it.user.name ?: "Unknown Author",
            creationDate = it.createdAt ?: "N/A"
        )
    }?: listOf()
}