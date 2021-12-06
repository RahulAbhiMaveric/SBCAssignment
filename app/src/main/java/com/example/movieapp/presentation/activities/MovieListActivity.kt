package com.example.movieapp.presentation.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.presentation.adapters.MovieListAdapter
import com.example.movieapp.databinding.ActivityMovieListBinding
import com.example.movieapp.models.MovieList
import com.example.movieapp.models.SearchItem
import com.example.movieapp.myutils.DataState
import com.example.movieapp.myutils.MyConstant.Companion.IMDB_ID
import com.example.movieapp.presentation.clicklistener.OnMovieItemClickListener
import com.example.movieapp.presentation.viewmodels.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MovieListActivity : AppCompatActivity(), OnMovieItemClickListener {
    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var binding: ActivityMovieListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.progressBar.visibility = View.VISIBLE
        handleIntent(intent)
        subscribeObservers()
    }

    /**
     * handle search query via onNewIntent instead of creating new activity each time
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            handleIntent(intent)
        }
    }

    /**
     * observe the changes in live data of @MovieListViewModel
     */
    private fun subscribeObservers() {
        viewModel.dataState.observe(this, { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is DataState.Success<MovieList?> -> {
                    binding.progressBar.visibility = View.GONE
                    val result = dataState.data
                    binding.movieListRecyclerView.layoutManager = GridLayoutManager(this, 2)
                    val adapter: MovieListAdapter? =
                        result?.search?.let {
                            MovieListAdapter(
                                it as List<SearchItem>,
                                this@MovieListActivity
                            )
                        }
                    adapter?.let {
                        binding.movieListRecyclerView.adapter = adapter
                    }
                    Timber.d("movie list=$result")
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    /**
     * inflate searchview in toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return true
    }

    /**
     * get search query and calls api for perticular search
     */
    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            binding.progressBar.visibility = View.VISIBLE
            val query = intent.getStringExtra(SearchManager.QUERY)
            Timber.d("search item=$query")
            viewModel.getMovieList(query.toString())
        }
    }

    /**
     * handles click listener on movie items and navigate to @see MovieDetailsActivity
     */
    override fun onMovieItemClick(movie: SearchItem) {
        Timber.d("movie imdbId=${movie.imdbID}")
        val intent = Intent(this@MovieListActivity,MovieDetailsActivity::class.java)
        intent.putExtra(IMDB_ID,movie.imdbID)
        startActivity(intent)
    }
}