package com.codemobile.mobilephonebuyersguideapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.codemobile.cmscb.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.fragment.FavouriteFragment
import com.codemobile.mobilephonebuyersguideapp.fragment.MobileFragment
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.activity.OnSortMobileListener
import com.codemobile.mobilephonebuyersguideapp.activity.onChangeFavouriteListener

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class SectionsPagerAdapter(private val context: Context,
                           fm: FragmentManager,
                           private val mobileArray: List<Mobile>,
                           private val lis: onChangeFavouriteListener) : FragmentPagerAdapter(fm) {

    var listnerAll: OnSortMobileListener? = null
    var listnerFav: OnSortMobileListener? = null
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val ff = MobileFragment(mobileArray, lis)
                listnerAll = ff
                return ff
            }
            else -> {
                val ff = FavouriteFragment(mobileArray, lis)
                listnerFav = ff
                return ff
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}