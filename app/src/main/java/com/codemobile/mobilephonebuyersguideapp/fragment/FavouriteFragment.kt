package com.codemobile.mobilephonebuyersguideapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.activity.MainActivity
import com.codemobile.mobilephonebuyersguideapp.activity.MobileInfoActivity
import com.codemobile.mobilephonebuyersguideapp.adapter.MobileAdapter
import com.codemobile.mobilephonebuyersguideapp.adapter.OnFavouriteClickListener
import com.codemobile.mobilephonebuyersguideapp.adapter.OnMobileClickListener
import com.codemobile.mobilephonebuyersguideapp.callback.CustomItemTouchHelperCallback

class FavouriteFragment: Fragment(), OnFavouriteClickListener {

    companion object {

        fun newInstance(): FavouriteFragment {
            return FavouriteFragment()
        }
    }

    private lateinit var rvMobiles: RecyclerView
    private lateinit var mAdapter: MobileAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onView(view)
        initRecyclerView()
        submitMobile()
    }

    private fun onView(view: View) {
        rvMobiles = view.findViewById(R.id.rv_mobile_favourite_list)
    }

    private fun initRecyclerView() {
        mAdapter = MobileAdapter((this as OnMobileClickListener), context)
        val callback = CustomItemTouchHelperCallback(mAdapter)
        val itemTouchHelper = ItemTouchHelper(callback)

        rvMobiles.adapter = mAdapter
        rvMobiles.layoutManager = LinearLayoutManager(context)
        rvMobiles.itemAnimator = DefaultItemAnimator()
        rvMobiles.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        itemTouchHelper.attachToRecyclerView(rvMobiles)
    }

    private fun submitMobile() {
        (context as? MainActivity)?.getFavouriteList()?.let { favouriteList ->
            mAdapter.submitList(favouriteList)
        }
    }

    override fun onMobileClick(mobile: Mobile) {
        MobileInfoActivity.startActivity(context, mobile)
    }

    override fun onChangeFavourite(mobile: Mobile) {
        (context as? MainActivity)?.setFavourite(mobile)
    }

    override fun onSwipedRemove(mobile: Mobile) {
        (context as? MainActivity)?.setFavourite(mobile)
    }

    fun onBindChangData(favouriteList: List<Mobile>) {
        if (::mAdapter.isInitialized)
            mAdapter.submitList(favouriteList)
    }
}
