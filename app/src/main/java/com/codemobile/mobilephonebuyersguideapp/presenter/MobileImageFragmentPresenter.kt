package com.codemobile.mobilephonebuyersguideapp.presenter

import com.codemobile.mobilephonebuyersguideapp.interfaces.MobileImageFragmentPresenterInterface

class MobileImageFragmentPresenter(private var view: MobileImageFragmentPresenterInterface) {

    private lateinit var imageURL: String

    fun setFragment(url: String) {
        reFormURL(url)
        view.setImage(imageURL)
    }

    private fun reFormURL(url: String) {
        if (!url.contains("http", true)) {
            imageURL = "https://$url"
        }
        imageURL = url
    }
}