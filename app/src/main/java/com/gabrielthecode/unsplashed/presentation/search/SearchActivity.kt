package com.gabrielthecode.unsplashed.presentation.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.gabrielthecode.unsplashed.R
import com.gabrielthecode.unsplashed.core.domain.Resource
import com.gabrielthecode.unsplashed.databinding.ActivitySearchBinding
import com.gabrielthecode.unsplashed.presentation.fullrespreview.PreviewManager
import com.gabrielthecode.unsplashed.presentation.search.SearchActivity.Companion.PHOTO_UI_MODEL
import com.gabrielthecode.unsplashed.presentation.search.details.PhotoDetailsActivity
import com.gabrielthecode.unsplashed.presentation.search.viewholders.PhotoOnClickListener
import com.gabrielthecode.unsplashed.presentation.search.viewholders.PhotoRecyclerViewAdapter
import com.gabrielthecode.unsplashed.presentation.search.viewholders.PhotoUiModel
import com.gabrielthecode.unsplashed.presentation.search.viewholders.toUiModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), PhotoOnClickListener {

    private val viewModel: SearchViewModel by viewModels()
    private var binding: ActivitySearchBinding? = null
    private lateinit var recyclerViewAdapter: PhotoRecyclerViewAdapter
    private var PhotoOnClickListener: PhotoOnClickListener = this
    private var previewManager: PreviewManager? = null
    private val defaultQuery = "Creative"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initViews()
        initRecyclerView()
        subscribeObserver()
        viewModel.searchPhotos(defaultQuery)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun showErrorStateLayout() {
        binding?.included?.layoutBadState?.isVisible = true
    }

    private fun hideErrorStateLayout() {
        binding?.included?.layoutBadState?.isVisible = false
    }

    private fun subscribeObserver() {
        viewModel.searchState.observe(
            this
        ) {
            when (it) {
                is Resource.Success -> {
                    hideErrorStateLayout()
                    hideLoadingProgress()
                    it.data?.toUiModels()?.let { items ->
                        populateRecyclerView(items)
                    }
                }

                is Resource.Progress -> {
                    showLoadingProgress()
                }

                is Resource.Failure -> {
                    hideLoadingProgress()
                    showErrorStateLayout()
                }

                else -> Unit
            }
        }
    }

    private fun populateRecyclerView(trips: List<PhotoUiModel>) {
        recyclerViewAdapter.setItems(trips.toMutableList())
        binding?.recyclerView?.scheduleLayoutAnimation()
    }

    private fun hideLoadingProgress() {
        binding?.refreshLayout?.isRefreshing = false
    }

    private fun showLoadingProgress() {
        binding?.refreshLayout?.isRefreshing = true
    }

    private fun initViews() {
        binding?.apply {
            refreshLayout.setOnRefreshListener {
                val query =
                    searchViewKeyword.query.toString().takeIf { it.isNotEmpty() } ?: defaultQuery
                viewModel.searchPhotos(query)
            }

            // perform set on query text listener event
            searchViewKeyword.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(q: String): Boolean {
                    viewModel.searchPhotos(q)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // do something when text changes
                    return false
                }
            })

            previewManager = PreviewManager(expandedView, containerlayout)
        }
    }

    private fun initRecyclerView() {
        recyclerViewAdapter = PhotoRecyclerViewAdapter(PhotoOnClickListener)
        binding?.recyclerView?.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 3)
            adapter = recyclerViewAdapter
        }
    }

    private fun previewImage(uiModel: PhotoUiModel, view: View) {
        previewManager?.show(
            view, uiModel.url,
            R.drawable.gallery_image_placeholder
        )
    }

    override fun openPhoto(photo: PhotoUiModel) {
        val intent = Intent(this, PhotoDetailsActivity::class.java)
        intent.putExtra(PHOTO_UI_MODEL, photo)
        this.startActivity(intent)
    }

    override fun showImageFullResolution(photo: PhotoUiModel, view: View) {
        previewImage(photo, view)
    }


    object Companion {
        const val PHOTO_UI_MODEL = "PHOTO_UI_MODEL"
    }
}

