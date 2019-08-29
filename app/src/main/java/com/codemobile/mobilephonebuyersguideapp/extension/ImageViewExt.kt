package com.codemobile.mobilephonebuyersguideapp.extension

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.setImageUrl(url: String) {
    Picasso.get()
        .load(url)
        .into(this)
}