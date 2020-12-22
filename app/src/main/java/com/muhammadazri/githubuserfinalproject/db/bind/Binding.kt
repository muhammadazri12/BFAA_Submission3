package com.muhammadazri.githubuserfinalproject.db.bind

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muhammadazri.githubuserfinalproject.R

@BindingAdapter("avatar")
fun avatar(imageView: ImageView, avatar: String) =
        Glide.with(imageView)
                .load(avatar)
                .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_person))
                .into(imageView)