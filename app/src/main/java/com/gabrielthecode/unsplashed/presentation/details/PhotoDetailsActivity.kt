package com.gabrielthecode.unsplashed.presentation.details

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gabrielthecode.unsplashed.R
import com.gabrielthecode.unsplashed.core.domain.Resource
import com.gabrielthecode.unsplashed.databinding.ActivityPhotoDetailsBinding
import com.gabrielthecode.unsplashed.presentation.search.SearchActivity.Companion.PHOTO_UI_MODEL
import com.gabrielthecode.unsplashed.presentation.search.viewholders.PhotoUiModel
import com.gabrielthecode.unsplashed.utils.supportsAndroid13
import com.gabrielthecode.unsplashed.utils.transformToDate
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

private const val DOWNLOAD_REQUEST_CODE = 1000
private const val IMAGE_DIRECTORY = "Unsplashed"
private const val IMAGE_QUALITY = 100

@AndroidEntryPoint
class PhotoDetailsActivity : AppCompatActivity() {
	private val viewModel: PhotoDetailsViewModel by viewModels()
	private var binding: ActivityPhotoDetailsBinding? = null
	private var photo: PhotoUiModel? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
		setContentView(binding?.root)

		photo = if (supportsAndroid13()) {
			intent.extras?.getParcelable(PHOTO_UI_MODEL, PhotoUiModel::class.java)
		} else {
			@Suppress("DEPRECATION")
			intent.extras?.getParcelable(PHOTO_UI_MODEL)
		}

		initViews()
		subscribeObserver()
	}

	override fun onDestroy() {
		super.onDestroy()
		binding = null
	}

	private fun initViews() {
		binding?.apply {
			Glide.with(this@PhotoDetailsActivity).load(photo?.url)
				.placeholder(R.drawable.gallery_image_placeholder)
				.error(R.drawable.gallery_image_placeholder)
				.apply(RequestOptions().centerCrop())
				.into(imageHeader)

			photo?.apply {
				sizeTextView.text = getString(R.string.resolution, "$height x $width")
				authorTextView.text = authorName
				descriptionTextView.text = description
				dateTextView.text = getString(R.string.created_at, creationDate.transformToDate())
				colorTextView.text = getString(R.string.dominant_color, dominantColor)
				likesTextView.text = resources.getQuantityString(R.plurals.likes, likes, likes)
				colorImageView.setBackgroundColor(Color.parseColor(dominantColor))
			}

			closeContainerLayout.setOnClickListener {
				finish()
			}

			downloadImageButton.setOnClickListener {
				checkAndDownloadImage()
			}
		}
	}

	private fun subscribeObserver() {
		viewModel.downloadState.observe(this) { result ->
			when (result) {
				is Resource.Progress -> {
					Toast.makeText(this, getString(R.string.downloading), Toast.LENGTH_SHORT).show()
				}
				is Resource.Success -> {
					result.data?.bitmap?.let { bitmap ->
						saveImageToFile(bitmap)
					}
				}
				is Resource.Failure -> {
					Toast.makeText(
						this,
						getString(R.string.failed_to_download_image, result.throwable.message),
						Toast.LENGTH_LONG
					).show()
				}
				is Resource.Idle -> Unit
			}
		}
	}

	private fun checkAndDownloadImage() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			// No need to request storage permission for Android 10+ (Q and above)
			photo?.url?.let { viewModel.downloadPhoto(it) }
		} else {
			// For Android 9 and below, check for WRITE_EXTERNAL_STORAGE permission
			if (ContextCompat.checkSelfPermission(
					this,
					WRITE_EXTERNAL_STORAGE
				) != PackageManager.PERMISSION_GRANTED
			) {
				ActivityCompat.requestPermissions(
					this,
					arrayOf(WRITE_EXTERNAL_STORAGE),
					1000
				)
			} else {
				// Permission granted, start the download
				photo?.url?.let { viewModel.downloadPhoto(it) }
			}
		}
	}

	// Handle permission result
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == DOWNLOAD_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			photo?.url?.let { viewModel.downloadPhoto(it) }
		} else {
			Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
		}
	}

	// Save the downloaded bitmap to file
	private fun saveImageToFile(bitmap: Bitmap) {
		var savedImageUri: Uri? = null

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			// Use MediaStore to save images for Android 10+ (Scoped Storage)
			val contentResolver = contentResolver
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
				showImageSavedSnackbar(uri)
			}
		} else {
			try {
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
				showImageSavedSnackbar(savedImageUri)
			} catch (e: Exception) {
				Toast.makeText(
					this@PhotoDetailsActivity,
					getString(R.string.failed_to_save_image, e.message),
					Toast.LENGTH_LONG
				).show()
			}
		}
	}

	private fun showImageSavedSnackbar(imageUri: Uri) {
		// Use Snackbar instead of Toast to show "View Image" option
		binding?.root?.let { rootView ->
			Snackbar.make(rootView, getString(R.string.image_saved), Snackbar.LENGTH_LONG)
				.setAction("View") {
					// Create an Intent to view the image in the gallery or another app
					val intent = Intent(Intent.ACTION_VIEW).apply {
						setDataAndType(imageUri, "image/*")
						flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
					}
					startActivity(intent)
				}.show()
		}
	}
}