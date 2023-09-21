package com.gabrielthecode.unsplashed.presentation.search.details

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gabrielthecode.unsplashed.R
import com.gabrielthecode.unsplashed.databinding.ActivityPhotoDetailsBinding
import com.gabrielthecode.unsplashed.presentation.search.SearchActivity.Companion.PHOTO_UI_MODEL
import com.gabrielthecode.unsplashed.presentation.search.viewholders.PhotoUiModel
import com.gabrielthecode.unsplashed.utils.extensions.transformToDate

class PhotoDetailsActivity : AppCompatActivity() {

    private var binding: ActivityPhotoDetailsBinding? = null
    private var photo: PhotoUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        photo = if (supportsAndroid13()) {
            intent.extras?.getParcelable(PHOTO_UI_MODEL, PhotoUiModel::class.java)
        } else {
            intent.extras?.getParcelable(PHOTO_UI_MODEL)
        }

        initViews()
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
                colorImageView.setBackgroundColor(Color.parseColor(dominantColor))
            }

            closeContainerLayout.setOnClickListener {
                finish()
            }

            downloadImageButton.setOnClickListener {
                Toast.makeText(
                    this@PhotoDetailsActivity,
                    getString(R.string.coming_soon),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun supportsAndroid13() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

}