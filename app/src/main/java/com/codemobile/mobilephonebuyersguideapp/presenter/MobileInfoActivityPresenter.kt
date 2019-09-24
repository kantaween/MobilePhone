package com.codemobile.mobilephonebuyersguideapp.presenter

import android.content.Intent
import com.codemobile.mobilephonebuyersguideapp.activity.MobileInfoActivity
import com.codemobile.mobilephonebuyersguideapp.interfaces.MobileInfoActivityPresenterInterface
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.models.MobileImage
import com.codemobile.mobilephonebuyersguideapp.service.MobileApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileInfoActivityPresenter(
    private var view: MobileInfoActivityPresenterInterface,
    private val service: MobileApiService
) {

    companion object {
        private const val ERROR_MSG_ONLOAD_IMAGE_FAIL = "Load image fail"
    }

    private lateinit var mImageList: List<MobileImage>
    private lateinit var mMobile: Mobile

    fun init(intent: Intent) {
        mMobile = intent.getParcelableExtra(MobileInfoActivity.EXTRA_MOBILE)
        view.setContent(mMobile)
    }

    fun loadMobileImages() {
        service.getMobileImage(mMobile.id).enqueue(mobileImageCallback)
    }

    private val mobileImageCallback = object : Callback<List<MobileImage>> {
        override fun onFailure(call: Call<List<MobileImage>>, t: Throwable) {
            view.showErrorMsg(ERROR_MSG_ONLOAD_IMAGE_FAIL)
        }

        override fun onResponse(call: Call<List<MobileImage>>, response: Response<List<MobileImage>>) {
            response.body()?.apply {
                if (this.isNotEmpty()) {
                    mImageList = this
                    view.setViewPagerAdapter(mImageList)
                }
            }
        }
    }
}