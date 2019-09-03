package com.codemobile.mobilephonebuyersguideapp.models

import android.os.Parcel
import android.os.Parcelable

data class Mobile(
    val brand: String,
    val description: String,
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Double,
    val thumbImageURL: String,
    var favourite: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brand)
        parcel.writeString(description)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeDouble(rating)
        parcel.writeString(thumbImageURL)
        parcel.writeByte(if (favourite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mobile> {
        override fun createFromParcel(parcel: Parcel): Mobile {
            return Mobile(parcel)
        }

        override fun newArray(size: Int): Array<Mobile?> {
            return arrayOfNulls(size)
        }
    }

}