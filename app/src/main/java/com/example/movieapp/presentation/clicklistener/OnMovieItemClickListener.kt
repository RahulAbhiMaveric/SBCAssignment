package com.example.movieapp.presentation.clicklistener

import com.example.movieapp.models.SearchItem

/**
 * interface @OnMovieItemClickListener to handle click on movie items
 */
interface OnMovieItemClickListener {
    fun onMovieItemClick(movie:SearchItem)
}