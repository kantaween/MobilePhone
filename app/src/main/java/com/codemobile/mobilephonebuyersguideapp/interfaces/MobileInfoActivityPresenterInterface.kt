package com.codemobile.mobilephonebuyersguideapp.interfaces

import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.models.MobileImage

interface MobileInfoActivityPresenterInterface {
    fun setViewPagerAdapter(imageList:List<MobileImage>)
    fun setContent(mobile: Mobile)
}