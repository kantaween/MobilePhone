package com.codemobile.mobilephonebuyersguideapp.activity

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.codemobile.cmscb.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.adapter.SectionsPagerAdapter
import com.codemobile.mobilephonebuyersguideapp.service.ApiManager
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), onChangeFavouriteListener {

    private var mMobileArray: List<Mobile> = listOf<Mobile>()
    private lateinit var sort: ImageView
    private lateinit var mAlertDialog: AlertDialog
    private var sortList = arrayOf<String>("Price low to high", "Price high to low", "Rating 5-1")

    var sectionsPagerAdapter : SectionsPagerAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sort = findViewById(R.id.iv_sort)
        sort.setOnClickListener() { createAlertDialogWithRadioButtonGroup() }

        loadMobile()
    }

    private fun handleViewPager() {
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, mMobileArray, this)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    private fun loadMobile() {
        ApiManager.mobileService.getMobileList().enqueue(mobileListCallback)
    }

    private fun createAlertDialogWithRadioButtonGroup() {

        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setSingleChoiceItems(sortList, -1, DialogInterface.OnClickListener { dialog, item ->
            when (item) {
                0 ->
                    sectionsPagerAdapter?.listnerAll?.sortByPriceLow2High()
                1 ->
                    sectionsPagerAdapter?.listnerAll?.sortByPriceHigh2Low()
                2 ->
                    sectionsPagerAdapter?.listnerAll?.sortByRating()
            }
            mAlertDialog.dismiss()
        })
        mAlertDialog = builder.create()
        mAlertDialog.show()

    }

    private val mobileListCallback = object : Callback<List<Mobile>> {
        override fun onFailure(call: Call<List<Mobile>>, t: Throwable) {
            Log.e("API_Call_Error", "$t")
        }

        override fun onResponse(call: Call<List<Mobile>>, response: Response<List<Mobile>>) {
            response.body()?.let {
                mMobileArray = it
                handleViewPager()
            }
        }
    }

    override fun onChangeData(mobileArray: List<Mobile>) {
        mMobileArray = mobileArray
        sectionsPagerAdapter?.listnerAll?.onBindChangData(mobileArray)
        sectionsPagerAdapter?.listnerFav?.onBindChangData(mobileArray)
    }
}

interface OnSortMobileListener {
    fun sortByPriceLow2High()
    fun sortByPriceHigh2Low()
    fun sortByRating()
    fun onBindChangData(mobileArray: List<Mobile>)
}

interface onChangeFavouriteListener {
    fun onChangeData(mobileArray: List<Mobile>)
}