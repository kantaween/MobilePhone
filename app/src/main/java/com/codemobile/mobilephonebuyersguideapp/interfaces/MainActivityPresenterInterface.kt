package com.codemobile.mobilephonebuyersguideapp.interfaces

import com.codemobile.mobilephonebuyersguideapp.models.Mobile

interface MainActivityPresenterInterface {
    fun updateData(mobileList: List<Mobile>)
    fun showErrorMessage(error: String)
}