package com.codemobile.mobilephonebuyersguideapp.presenter

import com.codemobile.mobilephonebuyersguideapp.interfaces.MainActivityPresenterInterface
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.service.MobileApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityPresenter(private val view: MainActivityPresenterInterface, private val service: MobileApiService) {

    companion object {

        private const val ERROR_MSG_ONLOAD_FAIL = "Load data fail"

        const val ALERTDIALOG_SORT_LOW2HIGH = 0
        const val ALERTDIALOG_SORT_HIGH2LOW = 1
        const val ALERTDIALOG_SORT_RATING = 2
    }

    private lateinit var mFavouriteList: List<Mobile>
    private lateinit var mMobileList: List<Mobile>

    fun loadLocalData() {
        val gson = GsonBuilder().create()
        val json: String? = Prefs.getString("Favourite", null)
        var dataList: List<Mobile> = listOf()
        json?.let {
            dataList = gson.fromJson(it, Array<Mobile>::class.java).toList()
        }
        mFavouriteList = dataList
        mMobileList = dataList
    }

    fun loadNewData() {
        service.getMobileList().enqueue(mobileListCallback)
    }

    fun savePrefs() {
        val gson = Gson()
        val json = gson.toJson(mFavouriteList)
        Prefs.putString("Favourite", json)
    }


    fun handleSort(item: Int) {
        when (item) {
            ALERTDIALOG_SORT_LOW2HIGH -> {
                mMobileList = mMobileList.sortedBy { it.price }
                mFavouriteList = mFavouriteList.sortedBy { it.price }
            }
            ALERTDIALOG_SORT_HIGH2LOW -> {
                mMobileList = mMobileList.sortedByDescending { it.price }
                mFavouriteList = mFavouriteList.sortedByDescending { it.price }
            }
            ALERTDIALOG_SORT_RATING -> {
                mMobileList = mMobileList.sortedByDescending { it.rating }
                mFavouriteList = mFavouriteList.sortedByDescending { it.rating }
            }
        }
        view.updateData(mMobileList, mFavouriteList)
    }

    fun setFavourite(targetMobile: Mobile) {
        mMobileList.singleOrNull { it == targetMobile }?.let { mobile ->
            mobile.favourite = !targetMobile.favourite
            mFavouriteList = if (mobile.favourite) {
                mFavouriteList.plus(targetMobile)
            } else {
                mFavouriteList.filterNot { it.id == targetMobile.id }
            }
            view.updateData(mMobileList, mFavouriteList)
        }
    }

    fun getMobileList(): List<Mobile> = mMobileList

    fun getFavouriteList(): List<Mobile> = mFavouriteList

    private val mobileListCallback = object : Callback<List<Mobile>> {
        override fun onFailure(call: Call<List<Mobile>>, t: Throwable) {
            view.showErrorMessage(ERROR_MSG_ONLOAD_FAIL)
        }

        override fun onResponse(call: Call<List<Mobile>>, response: Response<List<Mobile>>) {
            val res = response.body()
            if (!res.isNullOrEmpty()) {
                if (::mFavouriteList.isInitialized  && !mFavouriteList.isNullOrEmpty())
                    mFavouriteList.forEach { prefData ->
                        res.singleOrNull { it.id == prefData.id }?.favourite = prefData.favourite
                    }
                mMobileList = res
                view.updateData(mMobileList, mFavouriteList)
            }
        }
    }
}