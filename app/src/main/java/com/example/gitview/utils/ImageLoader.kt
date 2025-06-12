package com.example.gitview.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.gitview.R

object ImageLoader {
    fun loadImage(url: String?, imageView: ImageView) {
        Glide.with(imageView.context.applicationContext)
            .load(url)
            .placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_person_24)
            .into(imageView)
    }
}