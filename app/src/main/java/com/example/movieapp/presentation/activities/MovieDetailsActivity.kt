package com.example.movieapp.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMovieDetailsBinding
import com.example.movieapp.models.MovieDetails
import com.example.movieapp.myutils.DataState
import com.example.movieapp.myutils.MyConstant.Companion.IMDB_ID
import com.example.movieapp.presentation.viewmodels.MovieDetailsViewModel
import com.example.movieapp.presentation.viewmodels.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * @MovieDetailsActivity class responsible for a movie details
 */
@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {
    private val viewModel: MovieDetailsViewModel by viewModels()
    private lateinit var binding: ActivityMovieDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val imdbId = intent?.getStringExtra(IMDB_ID)
        Timber.d("imdb id = $imdbId")
        imdbId?.let { viewModel.getMovieDetailsFlow(it) }
        subscribeLivedata()
    }

    /**
     * observe api response
     */
    private fun subscribeLivedata() {

        viewModel.dataState.observe(this, { data ->
            when (data) {
                is DataState.Loading -> {
                    viewModel.setLoading(true)
                }
                is DataState.Success -> {
                    viewModel.setLoading(false)
                    data.data?.let {
                        viewModel.setMovieDetails(it)
                        title = it.title
                    }

                }
                is DataState.Error -> {
                    viewModel.setLoading(false)
                }
            }
        })

    }
}