package com.codemobile.mobilephonebuyersguideapp.activity

import android.content.ContextWrapper
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.adapter.SectionsPagerAdapter
import com.codemobile.mobilephonebuyersguideapp.extension.showToast
import com.codemobile.mobilephonebuyersguideapp.fragment.FavouriteFragment
import com.codemobile.mobilephonebuyersguideapp.fragment.MobileFragment
import com.codemobile.mobilephonebuyersguideapp.interfaces.MainActivityPresenterInterface
import com.codemobile.mobilephonebuyersguideapp.models.FragmentModel
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.presenter.MainActivityPresenter
import com.codemobile.mobilephonebuyersguideapp.service.ApiManager
import com.google.android.material.tabs.TabLayout
import com.pixplicity.easyprefs.library.Prefs


class MainActivity : AppCompatActivity(), MainActivityPresenterInterface {

    private val presenter = MainActivityPresenter(this, ApiManager.mobileService)

    private lateinit var mFragmentList: List<FragmentModel>
    private lateinit var mMobileFragment: FragmentModel
    private lateinit var mFavouriteFragment: FragmentModel
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout
    private lateinit var sortImage: ImageView
    private lateinit var mAlertDialog: AlertDialog
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    private val MOBILE_FRAGMENT_TITLE = "All"
    private val FAVOURITE_FRAGMENT_TITLE = "Favourite"
    private val SORT_PRICE_LOW_TO_HIGH_TEXT = "Price low to high"
    private val SORT_PRICE_HIGH_TO_LOW_TEXT = "Price high to low"
    private val SORT_RATING_TEXT = "Rating 5-1"

    private var sortList = arrayOf(SORT_PRICE_LOW_TO_HIGH_TEXT, SORT_PRICE_HIGH_TO_LOW_TEXT, SORT_RATING_TEXT)

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
        mMobileFragment = FragmentModel(MOBILE_FRAGMENT_TITLE, MobileFragment.newInstance())
        mFavouriteFragment = FragmentModel(FAVOURITE_FRAGMENT_TITLE, FavouriteFragment.newInstance())
        mFragmentList = listOf(mMobileFragment, mFavouriteFragment)
    }

    private fun handleViewPager() {
        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, mFragmentList)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    private fun createAlertDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setSingleChoiceItems(this.sortList, -1) { _, item ->
            presenter.handleSort(item)
        }

        mAlertDialog = builder.create()
        sortImage.setOnClickListener { mAlertDialog.show() }
    }

    override fun updateData(mobileList: List<Mobile>, favouriteList: List<Mobile>) {
        if (mAlertDialog.isShowing)
            mAlertDialog.dismiss()

        (mMobileFragment.fragment as? MobileFragment)?.onBindChangData(mobileList)
        (mFavouriteFragment.fragment as? FavouriteFragment)?.onBindChangData(favouriteList)
    }

    override fun showErrorMessage(error: String) {
        showToast(error)
    }

    fun setFavourite(mobile: Mobile) {
        presenter.setFavourite(mobile)
    }

    fun getMobileList(): List<Mobile> {
        return presenter.getMobileList()
    }

    fun getFavouriteList(): List<Mobile> {
        return presenter.getFavouriteList()
    }
}