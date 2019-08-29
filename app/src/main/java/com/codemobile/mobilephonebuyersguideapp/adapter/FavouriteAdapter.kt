package com.codemobile.mobilephonebuyersguideapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codemobile.cmscb.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.callback.CustomItemTouchHelperListener
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.extension.setImageUrl

class FavouriteAdapter(private var listener : OnMobileClickListener)
    : RecyclerView.Adapter<FavouriteItemViewHolder>(),
    CustomItemTouchHelperListener {

    val mobiles: List<Mobile>
        get() = _mobiles

    private var _mobiles: List<Mobile> = listOf<Mobile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavouriteItemViewHolder(parent)

    override fun onBindViewHolder(holder: FavouriteItemViewHolder, position: Int) {
        holder.bind(_mobiles[position], listener)
    }

    override fun getItemCount(): Int {
        return mobiles.count()
    }

    fun submitList(list: List<Mobile>) {
        _mobiles = list.filter { it.favourite }
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        return false
    }

    override fun onItemDismiss(position: Int) {
        listener.onSwipedRemove(mobiles[position])
    }

}

class FavouriteItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
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