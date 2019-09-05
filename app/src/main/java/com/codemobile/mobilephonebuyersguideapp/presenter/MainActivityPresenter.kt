package com.codemobile.mobilephonebuyersguideapp.presenter

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
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

    private val ALERTDIALOG_SORT_LOW2HIGH = 0
    private val ALERTDIALOG_SORT_HIGH2LOW = 1
    private val ALERTDIALOG_SORT_RATING = 2
    private val ERROR_MSG_ONLOAD_FAIL = "Load data fail"

    private lateinit var mMobileArray: List<Mobile>
    private var sortList = arrayOf("Price low to high", "Price high to low", "Rating 5-1")

    fun loadLocalData() {
        val gson = GsonBuilder().create()
        val json: String? = Prefs.getString("Favourite", null)
        var dataArray: List<Mobile> = listOf()
        json?.let {
            dataArray = gson.fromJson(it, Array<Mobile>::class.java).toList()
        }
        mMobileArray = dataArray
    }

    fun loadNewData() {
        service.getMobileList().enqueue(mobileListCallback)
    }

    fun savePrefs() {
        val gson = Gson()
        val json = gson.toJson(mMobileArray)
        Prefs.putString("Favourite", json)
    }

    fun initDialogBuilder(context: Context): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)

        builder.setSingleChoiceItems(this.sortList, -1) { _, item ->
            mMobileArray = when (item) {
                ALERTDIALOG_SORT_LOW2HIGH -> {
                    mMobileArray.sortedBy { it.price }
                }
                ALERTDIALOG_SORT_HIGH2LOW -> {
                    mMobileArray.sortedByDescending { it.price }
                }
                ALERTDIALOG_SORT_RATING -> {
                    mMobileArray.sortedByDescending { it.rating }
                }
                else -> mMobileArray
            }
            view.updateData(mMobileArray)
        }

        return builder
    }

    fun setFavourite(mobile: Mobile) {
        mMobileArray.single { it == mobile }.favourite = !mobile.favourite
        view.updateData(mMobileArray)
    }

    // For test
    fun setMobileList(mobileList: List<Mobile>) {
        mMobileArray = mobileList
    }

    fun getMobileList(): List<Mobile> = mMobileArray

    private val mobileListCallback = object : Callback<List<Mobile>> {
        override fun onFailure(call: Call<List<Mobile>>, t: Throwable) {
            Log.e("API_Call_Error", "$t")
            view.showErrorMessage(ERROR_MSG_ONLOAD_FAIL)
        }

        override fun onResponse(call: Call<List<Mobile>>, response: Response<List<Mobile>>) {
            val res = response.body()
            res?.let { newData ->
                if (::mMobileArray.isInitialized)
                    mMobileArray.forEach { prefData ->
                        newData.single { it.id == prefData.id }.favourite = prefData.favourite
                    }
                mMobileArray = newData
                view.updateData(mMobileArray)
            }
        }
    }
}