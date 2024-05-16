package com.pumpkin.pac.bean

import android.os.Parcel
import android.os.Parcelable
import com.pumpkin.mvvm.util.Constant

class GParameter(val isShowLoading: Boolean, val module: String = "", val orientation: Int = Constant.INVALID_ID) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt() != 0, parcel.readString()
        ?: "", parcel.readInt() ?: Constant.INVALID_ID)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(if (isShowLoading) 1 else 0)
        parcel.writeString(module)
        parcel.writeInt(orientation)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "GParameter(isShowLoading=$isShowLoading)"
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