package com.codemobile.mobilephonebuyersguideapp.activity

import android.content.ContextWrapper
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.adapter.SectionsPagerAdapter
import com.codemobile.mobilephonebuyersguideapp.fragment.FavouriteFragment
import com.codemobile.mobilephonebuyersguideapp.fragment.MobileFragment
import com.codemobile.mobilephonebuyersguideapp.presenter.MainActivityPresenter
import com.codemobile.mobilephonebuyersguideapp.presenter.MainActivityPresenterInterface
import com.google.android.material.tabs.TabLayout
import com.pixplicity.easyprefs.library.Prefs


class MainActivity : AppCompatActivity(), OnChangeFavouriteListener, MainActivityPresenterInterface {

    private val presenter = MainActivityPresenter(this)

    private lateinit var mFragmentList: List<Fragment>
    private lateinit var mMobileFragment: MobileFragment
    private lateinit var mFavouriteFragment: FavouriteFragment
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout
    private lateinit var sortImage: ImageView
    private lateinit var mAlertDialog: AlertDialog
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onView()
        initFragment()
        handleViewPager()
        createAlertDialog()

        presenter.loadLocalData()
        presenter.loadNewData()
    }

    override fun onStop() {
        super.onStop()
        presenter.savePrefs()
    }

    private fun onView() {
        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)
        sortImage = findViewById(R.id.iv_sort)
    }

    private fun initFragment() {
        mMobileFragment = MobileFragment.newInstance(this)
        mFavouriteFragment = FavouriteFragment.newInstance(this)
        mFragmentList = listOf(mMobileFragment, mFavouriteFragment)
    }

    private fun handleViewPager() {
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, mFragmentList)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    private fun createAlertDialog() {
        mAlertDialog = presenter.initDialogBuilder(this).create()
        sortImage.setOnClickListener { mAlertDialog.show() }
    }

    override fun updateData(mobileList: List<Mobile>) {
        if (mAlertDialog.isShowing)
            mAlertDialog.dismiss()

        mMobileFragment.onBindChangData(mobileList)
        mFavouriteFragment.onBindChangData(mobileList)
    }

    override fun setFavourite(mobile: Mobile) {
        presenter.setFavourite(mobile)
    }

    fun getMobileList(): List<Mobile> {
        return presenter.getMobileList()
    }
}

interface OnChangeFavouriteListener {
    fun setFavourite(mobile: Mobile)
}