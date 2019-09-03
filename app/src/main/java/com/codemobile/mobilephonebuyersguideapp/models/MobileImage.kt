package com.codemobile.mobilephonebuyersguideapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MobileImage(
    @SerializedName("id")
    val id: Int,

    @SerializedName("mobile_id")
    val mobile_id: Int,

    @SerializedName("url")
    val url: String
): Parcelable