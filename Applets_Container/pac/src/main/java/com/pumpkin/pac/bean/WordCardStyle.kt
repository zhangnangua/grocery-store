package com.pumpkin.pac.bean

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import com.pumpkin.pac.parseStrategy.IParsed

class WordCardStyle constructor(val id: Long, val icon: String, val name: String, val link: String, val parsed: IParsed? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            parcel.readParcelable(IParsed::class.java.getClassLoader(), IParsed::class.java)
        } else {
            parcel.readParcelable(IParsed::class.java.getClassLoader())
        }
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(icon)
        parcel.writeString(name)
        parcel.writeString(link)
        parcel.writeParcelable(parsed, flags)
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