package com.codemobile.mobilephonebuyersguideapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codemobile.cmscb.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.extension.setImageUrl

class MobileAdapter(private var listener : OnMobileClickListener)
    : RecyclerView.Adapter<MobileItemViewHolder>() {

    val mobiles: List<Mobile>
        get() = _mobiles

    private var _mobiles: List<Mobile> = listOf<Mobile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MobileItemViewHolder(parent)

    override fun onBindViewHolder(holder: MobileItemViewHolder, position: Int) {
        holder.bind(_mobiles[position], listener)
    }

    override fun getItemCount(): Int {
        return _mobiles.count()
    }

    fun submitList(list: List<Mobile>) {
        _mobiles = list
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

    fun bind(mobile: Mobile, listener : OnMobileClickListener) {
        tvMobileName.text = mobile.name
        tvMobileDiscription.text = mobile.description
        tvMobilePrice.text = "price: $${mobile.price}"
        tvMobileRating.text = "rating: ${mobile.rating}"
        ivMobileImage.setImageUrl(mobile.thumbImageURL)

        if (mobile.favourite) {
            ivFavouriteImage.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            ivFavouriteImage.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }

        itemView.setOnClickListener { listener.onMobileClick(mobile) }
        ivFavouriteImage.setOnClickListener { listener.onClickFavourite(ivFavouriteImage ,mobile) }
    }
}

interface OnMobileClickListener {
    fun onMobileClick(mobile: Mobile)
    fun onClickFavourite(ivFavouriteImage: ImageView, mobile: Mobile)
}