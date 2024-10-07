package com.gabrielthecode.unsplashed.presentation.details

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

private const val SAVE_REQUEST_CODE = 1000

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
		subscribeObservers()
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

	private fun subscribeObservers() {
		viewModel.downloadState.observe(this) { result ->
			when (result) {
				is Resource.Progress -> {
					Toast.makeText(this, getString(R.string.downloading), Toast.LENGTH_SHORT).show()
				}
				is Resource.Success -> {
					result.data?.bitmap?.let { bitmap ->
						viewModel.savePhoto(bitmap)
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

		viewModel.saveState.observe(this) { resource ->
			when (resource) {
				is Resource.Progress -> Unit
				is Resource.Success -> {
					resource.data?.let {
						showImageSavedSnackbar(it)
					} ?: run {
						Toast.makeText(
							this,
							getString(R.string.failed_to_save_image),
							Toast.LENGTH_LONG
						).show()
					}
				}
				is Resource.Failure -> {
					Toast.makeText(
						this,
						getString(R.string.failed_to_save_image, resource.throwable.message),
						Toast.LENGTH_LONG
					).show()
				}
				is Resource.Idle -> Unit
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
		if (requestCode == SAVE_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			photo?.url?.let { viewModel.downloadPhoto(it) }
		} else {
			Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
		}
	}

	private fun checkAndDownloadImage() {
		photo?.url?.let { viewModel.downloadPhoto(it) }
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