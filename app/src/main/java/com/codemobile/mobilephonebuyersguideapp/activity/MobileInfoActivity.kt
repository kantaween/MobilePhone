package com.codemobile.mobilephonebuyersguideapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.codemobile.cmscb.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.R
import com.codemobile.mobilephonebuyersguideapp.adapter.MobileImagePagerAdapter
import com.codemobile.mobilephonebuyersguideapp.models.MobileImage
import com.codemobile.mobilephonebuyersguideapp.service.ApiManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MobileInfoActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_MOBILE = "mobile"

        fun startActivity(context: Context, mobile: Mobile? = null) {
            var intent = Intent(context, MobileInfoActivity::class.java)
            intent.putExtra(EXTRA_MOBILE, mobile)
            context.startActivity(intent)
        }
    }

    private lateinit var vpImage: ViewPager
    private lateinit var tvMobileName: TextView
    private lateinit var tvMobileBrand: TextView
    private lateinit var tvMobilePrice: TextView
    private lateinit var tvMobileRating: TextView
    private lateinit var tvMobileDiscription: TextView
    private var mImageList: ArrayList<MobileImage>? = ArrayList<MobileImage>()

    private val mobileImageCallback = object : Callback<ArrayList<MobileImage>> {
        override fun onFailure(call: Call<ArrayList<MobileImage>>, t: Throwable) {
            Log.e("Call Api", "$t")
        }

        override fun onResponse(call: Call<ArrayList<MobileImage>>, response: Response<ArrayList<MobileImage>>) {
            mImageList = response.body()
            mImageList?.let {
                vpImage.adapter = MobileImagePagerAdapter(supportFragmentManager, it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moblie_info)

        val actionBar = supportActionBar

        actionBar!!.setDisplayHomeAsUpEnabled(true)

        vpImage = findViewById(R.id.vp_info_image)
        tvMobileName = findViewById(R.id.tv_info_name)
        tvMobileBrand = findViewById(R.id.tv_info_brand)
        tvMobilePrice = findViewById(R.id.tv_info_price)
        tvMobileRating = findViewById(R.id.tv_info_rating)
        tvMobileDiscription = findViewById(R.id.tv_info_discription)

        val mobile = intent.getParcelableExtra<Mobile>(EXTRA_MOBILE) ?: return

        loadMobileImages(mobile.id)
        showSongInformation(mobile)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadMobileImages(id: Int) {
        ApiManager.mobileService.getMobileImage(id).enqueue(mobileImageCallback)
    }

    private fun showSongInformation(mobile: Mobile) {
        tvMobileName.text = mobile.name
        tvMobileBrand.text = mobile.brand
        tvMobilePrice.text = "Price: $${mobile.price}"
        tvMobileRating.text = "Rating: ${mobile.rating}"
        tvMobileDiscription.text = mobile.description
    }
}
