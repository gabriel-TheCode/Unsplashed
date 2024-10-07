package com.gabrielthecode.unsplashed.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.gabrielthecode.unsplashed.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

private const val IMAGE_DIRECTORY = "Unsplashed"
private const val IMAGE_QUALITY = 100

class ImageStorageHelper @Inject constructor(@ApplicationContext private val context: Context) {
	fun savePhoto(bitmap: Bitmap): Uri {
		val savedImageUri: Uri?

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			val contentResolver = context.contentResolver
			val contentValues = ContentValues().apply {
				put(
					MediaStore.Images.Media.DISPLAY_NAME,
					"unsplash_${System.currentTimeMillis()}.jpg"
				)
				put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
				put(
					MediaStore.Images.Media.RELATIVE_PATH,
					Environment.DIRECTORY_PICTURES + "/$IMAGE_DIRECTORY"
				)
				put(MediaStore.Images.Media.IS_PENDING, true)
			}

			savedImageUri =
				contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
			savedImageUri?.let { uri ->
				contentResolver.openOutputStream(uri)?.use { outputStream ->
					bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
				}
				contentValues.clear()
				contentValues.put(MediaStore.Images.Media.IS_PENDING, false)
				contentResolver.update(uri, contentValues, null, null)
			}
		} else {
			val directory = File(
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
					.toString() + "/$IMAGE_DIRECTORY/"
			)
			if (!directory.exists()) {
				directory.mkdirs()
			}
			val file = File(directory, "unsplash_${System.currentTimeMillis()}.jpg")
			val outputStream = FileOutputStream(file)

			bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
			outputStream.flush()
			outputStream.close()

			savedImageUri = Uri.fromFile(file)
		}

		return savedImageUri ?: throw Exception(context.getString(R.string.failed_to_save_image))
	}
}
