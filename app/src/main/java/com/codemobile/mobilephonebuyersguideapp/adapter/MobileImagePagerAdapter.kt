package com.codemobile.mobilephonebuyersguideapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.codemobile.mobilephonebuyersguideapp.fragment.MobileImageFragment
import com.codemobile.mobilephonebuyersguideapp.models.MobileImage

class MobileImagePagerAdapter(supportFragmentManager: FragmentManager, private val imageArray: List<MobileImage>) : FragmentPagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        return MobileImageFragment(imageArray[position].url)
    }

    override fun getCount(): Int {
        return imageArray.count()
    }
}