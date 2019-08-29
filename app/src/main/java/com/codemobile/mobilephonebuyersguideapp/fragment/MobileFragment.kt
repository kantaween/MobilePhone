package com.codemobile.mobilephonebuyersguideapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codemobile.cmscb.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.activity.MobileInfoActivity
import com.codemobile.mobilephonebuyersguideapp.activity.OnSortMobileListener
import com.codemobile.mobilephonebuyersguideapp.activity.onChangeFavouriteListener
import com.codemobile.mobilephonebuyersguideapp.adapter.MobileAdapter
import com.codemobile.mobilephonebuyersguideapp.adapter.OnMobileClickListener

class MobileFragment(private var mMobileArray: List<Mobile>,
                     private var listener: onChangeFavouriteListener
): Fragment(), OnMobileClickListener, OnSortMobileListener {

    private lateinit var rvMobiles: RecyclerView
    lateinit var mAdapter: MobileAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMobiles = view.findViewById(R.id.rv_mobile_all_list)
        mAdapter = MobileAdapter(this)
        rvMobiles.adapter = mAdapter
        rvMobiles.layoutManager = LinearLayoutManager(context)
        rvMobiles.itemAnimator = DefaultItemAnimator()
        rvMobiles.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        mAdapter.submitList(mMobileArray)
    }

    override fun onMobileClick(mobile: Mobile) {
        context?.let {
            MobileInfoActivity.startActivity(it, mobile)
        }
    }

    override fun onClickFavourite(ivFavouriteImage: ImageView, mobile: Mobile) {
        mobile.favourite = !mobile.favourite
        if (mobile.favourite) {
            ivFavouriteImage.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            ivFavouriteImage.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }
        listener.onChangeData(mMobileArray)
    }

    override fun onSwipedRemove(mobile: Mobile) {}

    override fun sortByPriceLow2High() {
        val list : List<Mobile> = mMobileArray.sortedBy {
            it.price
        }
        mAdapter.submitList(list)
    }

    override fun sortByPriceHigh2Low() {
        val list : List<Mobile> = mMobileArray.sortedByDescending {
            it.price
        }
        mAdapter.submitList(list)
    }

    override fun sortByRating() {
        val list : List<Mobile> = mMobileArray.sortedByDescending {
            it.rating
        }
        mAdapter.submitList(list)
    }

    override fun onBindChangData(mobileArray: List<Mobile>) {
        mMobileArray = mobileArray
        mAdapter.submitList(mMobileArray)
    }
}
