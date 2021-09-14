package com.charlezz.mvi.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import android.util.TypedValue
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.charlezz.mvi.R
import com.charlezz.mvi.databinding.ActivityPhotoBinding
import com.charlezz.mvi.ui.base.BaseActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PhotoActivity : BaseActivity<PhotoAction, PhotoState, PhotoViewModel>() {

    @Inject
    lateinit var assistedFactory: PhotoViewModel.PhotoViewModelFactory

    override val viewModel: PhotoViewModel by viewModels {
        PhotoViewModel.Factory(assistedFactory)
    }

    private val adapter = PhotoAdapter()

    private val layoutManager: GridLayoutManager by lazy {
        GridLayoutManager(this, calculateSpanCount())
    }

    private val binding: ActivityPhotoBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_photo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initData()
    }

    private fun initUI() {
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager

        binding.search.setOnClickListener {
            viewModel.setAction(
                PhotoAction.SearchKeyword(
                    binding.editText.text.toString()
                )
            )
        }

        adapter.addLoadStateListener { loadStates ->
            binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
        }
    }

    private fun initData() {
        viewModel.setAction(PhotoAction.Initialize)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        layoutManager.spanCount = calculateSpanCount()
    }

    private fun calculateSpanCount(): Int {
        return resources.displayMetrics.widthPixels / TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            105.0f,
            resources.displayMetrics
        ).toInt()
    }

    override fun render(state: PhotoState) = when(state) {
        is PhotoState.List -> handleListState(state)
        is PhotoState.Unitialized -> Unit
    }

    private fun handleListState(state: PhotoState.List) {
        lifecycleScope.launch {
            adapter.submitData(state.photoData)
        }
    }

}
