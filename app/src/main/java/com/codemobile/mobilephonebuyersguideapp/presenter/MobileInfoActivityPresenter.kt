package com.codemobile.mobilephonebuyersguideapp.presenter

import android.content.Intent
import android.util.Log
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.activity.MobileInfoActivity
import com.codemobile.mobilephonebuyersguideapp.models.MobileImage
import com.codemobile.mobilephonebuyersguideapp.service.ApiManager
import com.codemobile.mobilephonebuyersguideapp.service.MobileApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileInfoActivityPresenter(private var view: MobileInfoActivityPresenterInterface, private val service: MobileApiService) {

    private lateinit var mImageList: List<MobileImage>
    private lateinit var mMobile: Mobile

    fun init(intent: Intent) {
        getMobileList(intent)
        loadMobileImages(mMobile.id)
        view.setContent(mMobile)
    }

    private fun getMobileList(intent: Intent) {
        mMobile = intent.getParcelableExtra(MobileInfoActivity.EXTRA_MOBILE)
    }

    private fun loadMobileImages(id: Int) {
        service.getMobileImage(id).enqueue(mobileImageCallback)
    }

    private val mobileImageCallback = object : Callback<List<MobileImage>> {
        override fun onFailure(call: Call<List<MobileImage>>, t: Throwable) {
            Log.e("Call Api", "$t")
        }

        override fun onResponse(call: Call<List<MobileImage>>, response: Response<List<MobileImage>>) {
            response.body()?.apply {
                mImageList = this
                view.setViewPagerAdapter(mImageList)
            }
        }
    }
}

interface MobileInfoActivityPresenterInterface {
    fun setViewPagerAdapter(imageList:List<MobileImage>)
    fun setContent(mobile: Mobile)
}