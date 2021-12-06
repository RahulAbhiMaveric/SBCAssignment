package com.example.movieapp.myutils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * BindingUtils class is binding adapter class for custom url
 * @see DataBinding library
 */
class BindingUtils {
    companion object {
        /**
         * @param url
         * @param imageView
         * it will load image @param url with the help of glide
         * library into the @param imageView
         */
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(imageView: ImageView, url: String?) {
            url?.let {
                Glide.with(imageView.context).load(url).into(imageView)
            }

        }
    }
}