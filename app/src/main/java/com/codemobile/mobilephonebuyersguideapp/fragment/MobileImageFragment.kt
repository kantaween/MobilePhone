package com.codemobile.mobilephonebuyersguideapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.interfaces.MobileImageFragmentPresenterInterface
import com.codemobile.mobilephonebuyersguideapp.presenter.MobileImageFragmentPresenter
import kotlinx.android.synthetic.main.fragment_mobile_image.*

class MobileImageFragment(private var imageURL: String): Fragment(), MobileImageFragmentPresenterInterface {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mobile_image, container, false)
    }

    private val presenter = MobileImageFragmentPresenter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setFragment(imageURL)
    }

    override fun setImage(url: String) {
        context?.apply {
            Glide.with(this).load(url).into(iv_info_image)
        }
    }
}