package com.valeserber.githubchallenge.util

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("convertLongToString")
fun TextView.convertLongToString(number: Long?) {
    text = number?.toString() ?: "Loading..."
}