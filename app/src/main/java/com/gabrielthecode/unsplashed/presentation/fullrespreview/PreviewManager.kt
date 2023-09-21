package com.gabrielthecode.unsplashed.presentation.fullrespreview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class PreviewManager(
	private val expandedView: ImageView,
	private val containerView: View
) {
	private val alphaSet: Array<Float> = arrayOf(0.0f, 0.1f, 0.2f, 0.25f, 0.5f, 0.75f, 1.0f)
	private var currentAnimator: Animator? = null

	fun show(view: View, uri: String, @DrawableRes placeholderId: Int) {
		shortAnimationDuration = 300
		expandedView.load(uri, placeholderId) {
			zoomImageFromThumb(view, containerView, expandedView)
		}
	}

	var shortAnimationDuration: Int = 0
	fun zoomImageFromThumb(
		thumbView: View,
		containerLayout: View,
		expandedImageView: ImageView,
	) {
		// If there's an animation in progress, cancel it
		// immediately and proceed with this one.
		currentAnimator?.cancel()
		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step involves lots of math. Yay, math.
		val startBoundsInt = Rect()
		val finalBoundsInt = Rect()
		val globalOffset = Point()
		// The start bounds are the global visible rectangle of the thumbnail,
		// and the final bounds are the global visible rectangle of the container
		// view. Also set the container view's offset as the origin for the
		// bounds, since that's the origin for the positioning animation
		// properties (X, Y).
		thumbView.getGlobalVisibleRect(startBoundsInt)
		containerLayout.getGlobalVisibleRect(finalBoundsInt, globalOffset)
		startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
		finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)
		val startBounds = RectF(startBoundsInt)
		val finalBounds = RectF(finalBoundsInt)
		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the "center crop" technique. This prevents undesirable
		// stretching during the animation. Also calculate the start scaling
		// factor (the end scaling factor is always 1.0).
		val startScale: Float
		if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
			// Extend start bounds horizontally
			startScale = startBounds.height() / finalBounds.height()
			val startWidth: Float = startScale * finalBounds.width()
			val deltaWidth: Float = (startWidth - startBounds.width()) / 2
			startBounds.left -= deltaWidth.toInt()
			startBounds.right += deltaWidth.toInt()
		} else {
			// Extend start bounds vertically
			startScale = startBounds.width() / finalBounds.width()
			val startHeight: Float = startScale * finalBounds.height()
			val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
			startBounds.top -= deltaHeight.toInt()
			startBounds.bottom += deltaHeight.toInt()
		}
		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins, it will position the zoomed-in view in the place of the
		// thumbnail.
//        thumbView.alpha = 0f
		expandedImageView.visibility = View.VISIBLE
		// Set the pivot point for SCALE_X and SCALE_Y transformations
		// to the top-left corner of the zoomed-in view (the default
		// is the center of the view).
		expandedImageView.pivotX = 0f
		expandedImageView.pivotY = 0f
		// Construct and run the parallel animation of the four translation and
		// scale properties (X, Y, SCALE_X, and SCALE_Y).
		currentAnimator = AnimatorSet().apply {
			play(
				ObjectAnimator.ofFloat(
					expandedImageView,
					View.X,
					startBounds.left,
					finalBounds.left
				)
			).apply {
				with(
					ObjectAnimator.ofFloat(
						expandedImageView,
						View.Y,
						startBounds.top,
						finalBounds.top
					)
				)
				with(
					ObjectAnimator.ofFloat(
						expandedImageView,
						View.SCALE_X,
						startScale,
						1f
					)
				)
				with(
					ObjectAnimator.ofFloat(
						expandedImageView,
						View.SCALE_Y,
						startScale,
						1f
					)
				)
				with(
					ObjectAnimator.ofFloat(
						expandedImageView,
						"alpha",
						*alphaSet.toFloatArray()
					)
				)
			}
			duration = shortAnimationDuration.toLong()
			interpolator = DecelerateInterpolator()
			addListener(object : AnimatorListenerAdapter() {
				override fun onAnimationEnd(animation: Animator) {
					currentAnimator = null
				}

				override fun onAnimationCancel(animation: Animator) {
					currentAnimator = null
				}
			})
			start()
		}
		// Upon clicking the zoomed-in image, it should zoom back down
		// to the original bounds and show the thumbnail instead of
		// the expanded image.
		expandedImageView.setOnClickListener {
			currentAnimator?.cancel()
			// Animate the four positioning/sizing properties in parallel,
			// back to their original values.
			currentAnimator = AnimatorSet().apply {
				play(
					ObjectAnimator.ofFloat(
						expandedImageView,
						View.X,
						startBounds.left
					)
				).apply {
					with(
						ObjectAnimator.ofFloat(
							expandedImageView,
							View.Y,
							startBounds.top
						)
					)
					with(
						ObjectAnimator.ofFloat(
							expandedImageView,
							View.SCALE_X,
							startScale
						)
					)
					with(
						ObjectAnimator.ofFloat(
							expandedImageView,
							View.SCALE_Y,
							startScale
						)
					)
					with(
						ObjectAnimator.ofFloat(
							expandedImageView,
							"alpha",
							*alphaSet.reversed().toFloatArray()
						)
					)
				}
				duration = shortAnimationDuration.toLong()
				interpolator = DecelerateInterpolator()
				addListener(object : AnimatorListenerAdapter() {
					override fun onAnimationEnd(animation: Animator) {
						thumbView.alpha = 1f
						expandedImageView.visibility = View.GONE
						currentAnimator = null
					}

					override fun onAnimationCancel(animation: Animator) {
						thumbView.alpha = 1f
						expandedImageView.visibility = View.GONE
						currentAnimator = null
					}
				})
				start()
			}
		}
	}
}

fun ImageView.load(path: String, @DrawableRes placeholder: Int, onLoaded: () -> Unit) {
	Glide.with(context).asBitmap().load(path).placeholder(placeholder)
		.listener(object : RequestListener<Bitmap> {
			override fun onLoadFailed(
				e: GlideException?,
				model: Any?,
				target: Target<Bitmap>,
				isFirstResource: Boolean
			): Boolean {
				return false
			}

			override fun onResourceReady(
				resource: Bitmap,
				model: Any,
				target: Target<Bitmap>?,
				dataSource: DataSource,
				isFirstResource: Boolean
			): Boolean {
				onLoaded()
				return false
			}
		}).into(this)
}