package com.codemobile.mobilephonebuyersguideapp.service

import com.codemobile.mobilephonebuyersguideapp.models.Mobile
import com.codemobile.mobilephonebuyersguideapp.models.MobileImage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MobileApiService {

    @GET("api/mobiles/")
    fun getMobileList(): Call<List<Mobile>>

    @GET("/api/mobiles/{mobile_id}/images/")
    fun getMobileImage(@Path("mobile_id")mobile_id: Int): Call<List<MobileImage>>
}
