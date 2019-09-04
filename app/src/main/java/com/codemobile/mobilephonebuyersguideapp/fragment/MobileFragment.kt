package com.codemobile.mobilephonebuyersguideapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.activity.MainActivity
import com.codemobile.mobilephonebuyersguideapp.activity.MobileInfoActivity
import com.codemobile.mobilephonebuyersguideapp.adapter.MobileAdapter
import com.codemobile.mobilephonebuyersguideapp.adapter.OnMobileClickListener

class MobileFragment: Fragment(), OnMobileClickListener {

    companion object {

        fun newInstance(): MobileFragment = MobileFragment()
    }

    private lateinit var rvMobiles: RecyclerView
    private lateinit var mAdapter: MobileAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onView(view)
        initRecyclerView()
        submitMobile()
    }

    private fun onView(view: View) {
        rvMobiles = view.findViewById(R.id.rv_mobile_all_list)
    }

    private fun initRecyclerView() {
        mAdapter = MobileAdapter(this, context)
        rvMobiles.adapter = mAdapter
        rvMobiles.layoutManager = LinearLayoutManager(context)
        rvMobiles.itemAnimator = DefaultItemAnimator()
        rvMobiles.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    private fun submitMobile() {
        (context as? MainActivity)?.getMobileList()?.let { mAdapter.submitList(it) }
    }

    override fun onMobileClick(mobile: Mobile) {
        MobileInfoActivity.startActivity(context, mobile)
    }

    override fun onChangeFavourite(mobile: Mobile) {
        (context as? MainActivity)?.setFavourite(mobile)
    }

    fun onBindChangData(mobileList: List<Mobile>) {
        if (::mAdapter.isInitialized)
            mAdapter.submitList(mobileList)
    }
}
