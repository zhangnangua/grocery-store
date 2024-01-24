package com.pumpkin.pac.bean

import android.os.Parcel
import android.os.Parcelable

class WordCardStyle constructor(val id: Long, val icon: String, val name: String, val link: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(icon)
        parcel.writeString(name)
        parcel.writeString(link)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WordCardStyle> {
        override fun createFromParcel(parcel: Parcel): WordCardStyle {
            return WordCardStyle(parcel)
        }

        override fun newArray(size: Int): Array<WordCardStyle?> {
            return arrayOfNulls(size)
        }
    }

}