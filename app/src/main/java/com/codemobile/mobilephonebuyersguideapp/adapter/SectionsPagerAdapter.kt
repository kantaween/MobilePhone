package com.codemobile.mobilephonebuyersguideapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.codemobile.mobilephonebuyersguideapp.models.FragmentModel

class SectionsPagerAdapter(private val context: Context,
                           fm: FragmentManager,
                           private val fragmentList: List<FragmentModel>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position].fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentList[position].title
    }

    override fun getCount(): Int {
        return fragmentList.count()
    }
}