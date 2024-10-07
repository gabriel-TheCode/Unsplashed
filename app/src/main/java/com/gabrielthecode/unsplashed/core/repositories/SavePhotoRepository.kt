package com.gabrielthecode.unsplashed.core.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.gabrielthecode.unsplashed.utils.ImageStorageHelper
import javax.inject.Inject

class SavePhotoRepository @Inject constructor(
	private val imageStorageHelper: ImageStorageHelper
) {
	fun savePhoto(bitmap: Bitmap): Uri {
		return imageStorageHelper.savePhoto(bitmap)
	}
}
