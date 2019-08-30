package com.codemobile.mobilephonebuyersguideapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codemobile.mobilephonebuyersguideapp.R
import kotlinx.android.synthetic.main.fragment_mobile_image.*

class MobileImageFragment(private var imageURL: String): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mobile_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!imageURL.contains("http", true)) {
            imageURL = "https://$imageURL"
        }
        context?.let {
            Glide.with(it).load(imageURL).into(iv_info_image)
        }
    }
}