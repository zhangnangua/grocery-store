package com.pumpkin.pac.bean

import android.os.Parcel
import android.os.Parcelable
import com.pumpkin.data.provider.IGParameter
import com.pumpkin.mvvm.util.Constant

class GParameter(val notShowLoading: Boolean, val module: String = "", val orientation: Int = Constant.INVALID_ID) : Parcelable ,IGParameter{
    constructor(parcel: Parcel) : this(parcel.readInt() != 0, parcel.readString()
        ?: "", parcel.readInt() ?: Constant.INVALID_ID)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(if (notShowLoading) 1 else 0)
        parcel.writeString(module)
        parcel.writeInt(orientation)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "GParameter(isShowLoading=$notShowLoading)"
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