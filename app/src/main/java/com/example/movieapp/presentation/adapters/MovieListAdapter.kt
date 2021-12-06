package com.example.movieapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.models.SearchItem
import com.example.movieapp.presentation.clicklistener.OnMovieItemClickListener

/**
 * @MovieListAdapter class shows movie list
 */
class MovieListAdapter(private val list: List<SearchItem>,private val mListener:OnMovieItemClickListener) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {
    /**
     * @MovieViewHolder is custom viewholder class
     */
    class MovieViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(searchItem: SearchItem,listener:OnMovieItemClickListener) {
            binding.items = searchItem
            binding.onClick=listener
            binding.executePendingBindings()
        }
    }

    /**
     * inflate movie item layout to recycler view
     * @param parent
     * @param viewType
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding: MovieItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.movie_item, parent,false)
        return MovieViewHolder(binding)
    }

    /**
     * onBindViewHolder
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position],mListener)
    }

    /**
     * @return size of list
     */
    override fun getItemCount(): Int {
        return list.size
    }
}