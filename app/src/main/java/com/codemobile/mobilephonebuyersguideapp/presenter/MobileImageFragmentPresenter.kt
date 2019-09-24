package com.codemobile.mobilephonebuyersguideapp.presenter

import com.codemobile.mobilephonebuyersguideapp.interfaces.MobileImageFragmentPresenterInterface

class MobileImageFragmentPresenter(private var view: MobileImageFragmentPresenterInterface) {

    companion object {
        private const val BEGIN_POSITION = 0
        private const val FINAL_POSITION = 3
    }

    private lateinit var imageURL: String

    fun setFragment(url: String) {
        reFormURL(url)
        view.setImage(imageURL)
    }

    private fun reFormURL(url: String) {
        if (!url.substring(BEGIN_POSITION, FINAL_POSITION).contains("http", true)) {
            imageURL = "https://$url"
        }
        imageURL = url
    }
}