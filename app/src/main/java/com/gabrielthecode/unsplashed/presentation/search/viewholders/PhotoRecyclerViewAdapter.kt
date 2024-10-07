package com.gabrielthecode.unsplashed.presentation.search.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gabrielthecode.unsplashed.R
import com.gabrielthecode.unsplashed.databinding.PhotoItemAdapterBinding

class PhotoRecyclerViewAdapter(
	private val onOpenPhoto: (photo: PhotoUiModel) -> Unit,
	private val onDisplayFullResolution: (photo: PhotoUiModel, view: View) -> Unit
) :
	RecyclerView.Adapter<PhotoRecyclerViewAdapter.NewsViewHolder>() {
	private lateinit var binding: PhotoItemAdapterBinding
	private var photos: List<PhotoUiModel> = listOf()
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
		binding =
			PhotoItemAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return NewsViewHolder(binding)
	}

	override fun getItemCount(): Int {
		return photos.size
	}

	override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
		val photo = photos[position]
		holder.apply {
			Glide.with(holder.itemView.context).load(photo.thumbnail)
				.placeholder(R.color.light_gray)
				.error(R.color.light_gray)
				.apply(RequestOptions().centerCrop())
				.into(photoImageView)

			photoImageView.setOnClickListener {
				onOpenPhoto(photo)
			}

			photoImageView.setOnLongClickListener {
				onDisplayFullResolution(photo, it)
				return@setOnLongClickListener true
			}
		}
	}

	fun setItems(items: List<PhotoUiModel>) {
		photos = emptyList()
		photos = items
		notifyDataSetChanged()
	}

	class NewsViewHolder(binding: PhotoItemAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
		val photoImageView: ImageView = binding.photoImageView
	}
}
