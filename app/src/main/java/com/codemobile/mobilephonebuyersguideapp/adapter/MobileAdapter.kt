package com.codemobile.mobilephonebuyersguideapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.callback.CustomItemTouchHelperListener
import com.codemobile.mobilephonebuyersguideapp.extension.context
import com.codemobile.mobilephonebuyersguideapp.extension.setImageUrl

class MobileAdapter(private var listener: OnMobileClickListener) : RecyclerView.Adapter<MobileItemViewHolder>(),
    CustomItemTouchHelperListener {

    private var mMobileList: List<Mobile> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MobileItemViewHolder(parent)

    override fun onBindViewHolder(holder: MobileItemViewHolder, position: Int) {
        holder.bind(mMobileList[position], listener)
    }

    override fun getItemCount(): Int {
        return mMobileList.count()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        return false
    }

    override fun onItemDismiss(position: Int) {
        (listener as? OnFavouriteClickListener)?.onSwipedRemove(mMobileList[position])
    }

    fun submitList(list: List<Mobile>) {
        mMobileList = list
        notifyDataSetChanged()
    }
}

class MobileItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_mobile, parent, false)
) {

    private val ivMobileImage: ImageView = itemView.findViewById(R.id.iv_mobile_image)
    private val tvMobileName: TextView = itemView.findViewById(R.id.tv_mobile_name)
    private val tvMobileDiscription: TextView = itemView.findViewById(R.id.tv_mobile_discription)
    private val tvMobilePrice: TextView = itemView.findViewById(R.id.tv_mobile_price)
    private val tvMobileRating: TextView = itemView.findViewById(R.id.tv_mobile_rating)
    private val ivFavouriteImage: ImageView = itemView.findViewById(R.id.iv_favourite_image)

    fun bind(mobile: Mobile, listener: OnMobileClickListener) {
        tvMobileName.text = mobile.name
        tvMobileDiscription.text = mobile.description
        tvMobilePrice.text = context().getString(R.string.price_text, mobile.price)
        tvMobileRating.text = context().getString(R.string.rating_text, mobile.rating)
        ivMobileImage.setImageUrl(mobile.thumbImageURL)

        if (mobile.favourite) {
            ivFavouriteImage.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            ivFavouriteImage.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }

        itemView.setOnClickListener { listener.onMobileClick(mobile) }
        ivFavouriteImage.setOnClickListener { listener.onChangeFavourite(mobile) }
    }
}

interface OnMobileClickListener {
    fun onMobileClick(mobile: Mobile)
    fun onChangeFavourite(mobile: Mobile)
}

interface OnFavouriteClickListener : OnMobileClickListener {
    fun onSwipedRemove(mobile: Mobile)
}