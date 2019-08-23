package com.valeserber.githubchallenge.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.valeserber.githubchallenge.R
import com.valeserber.githubchallenge.domain.Owner

@BindingAdapter("convertLongToString")
fun TextView.convertLongToString(number: Long?) {
    text = when(number) {
        null -> context.getString(R.string.empty_string)
        else -> number.toString()
    }
}

@BindingAdapter("ownerName")
fun TextView.setOwnerName(owner: Owner?) {
    text = when (owner) {
        null -> context.getString(R.string.owner_name)
        else -> context.getString(R.string.owner_name_with_param, owner.name)
    }
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(imageUrl: String?) {
    imageUrl?.let {
        val imgUri = imageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            .apply(
                RequestOptions()
            )
            .into(this)
    }
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(owner: Owner?) {
    owner?.let {
        loadImage(it.avatarUrl)
    }
}