package com.pumpkin.pac.bean

import android.os.Parcel
import android.os.Parcelable

class GParameter(val isInternal: Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt() != 0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(if (isInternal) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "GParameter(isInternal=$isInternal)"
    }

    companion object CREATOR : Parcelable.Creator<GParameter> {
        override fun createFromParcel(parcel: Parcel): GParameter {
            return GParameter(parcel)
        }

        override fun newArray(size: Int): Array<GParameter?> {
            return arrayOfNulls(size)
        }
    }


}