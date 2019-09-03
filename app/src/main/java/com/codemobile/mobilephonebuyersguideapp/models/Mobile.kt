package com.codemobile.mobilephonebuyersguideapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mobile(
    @SerializedName("brand")
    val brand: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("rating")
    val rating: Double,

    @SerializedName("thumbImageURL")
    val thumbImageURL: String,

    @SerializedName("favourite")
    var favourite: Boolean = false

) : Parcelable