package com.codemobile.mobilephonebuyersguideapp.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.adapter.MobileImagePagerAdapter
import com.codemobile.mobilephonebuyersguideapp.models.MobileImage
import com.codemobile.mobilephonebuyersguideapp.presenter.MobileInfoActivityPresenter
import com.codemobile.mobilephonebuyersguideapp.presenter.MobileInfoActivityPresenterInterface

class MobileInfoActivity : AppCompatActivity(), MobileInfoActivityPresenterInterface {

    companion object {
        const val EXTRA_MOBILE = "mobile"

        fun startActivity(context: Context?, mobile: Mobile? = null) {
            context?.apply {
                val intent = Intent(context, MobileInfoActivity::class.java)
                intent.putExtra(EXTRA_MOBILE, mobile)
                this.startActivity(intent)
            }
        }
    }

    private val presenter = MobileInfoActivityPresenter(this)

    private lateinit var vpImage: ViewPager
    private lateinit var tvMobileName: TextView
    private lateinit var tvMobileBrand: TextView
    private lateinit var tvMobilePrice: TextView
    private lateinit var tvMobileRating: TextView
    private lateinit var tvMobileDiscription: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moblie_info)
        onView()
        setActBar()
        presenter.init(intent)
    }

    private fun onView() {
        vpImage = findViewById(R.id.vp_info_image)
        tvMobileName = findViewById(R.id.tv_info_name)
        tvMobileBrand = findViewById(R.id.tv_info_brand)
        tvMobilePrice = findViewById(R.id.tv_info_price)
        tvMobileRating = findViewById(R.id.tv_info_rating)
        tvMobileDiscription = findViewById(R.id.tv_info_discription)
    }

    private fun setActBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun setViewPagerAdapter(imageList: List<MobileImage>) {
        vpImage.adapter = MobileImagePagerAdapter(supportFragmentManager, imageList)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun setContent(mobile: Mobile) {
        tvMobileName.text = mobile.name
        tvMobileBrand.text = mobile.brand
        tvMobilePrice.text = getString(R.string.price_text, mobile.price)
        tvMobileRating.text = getString(R.string.rating_text, mobile.rating)
        tvMobileDiscription.text = mobile.description
    }
}
