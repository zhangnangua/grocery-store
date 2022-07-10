package com.howie.multiple_process.bean

import android.os.Parcel
import android.os.Parcelable

class Book
@JvmOverloads
constructor(var bookId: Int = 0, var bookName: String? = "", var bookDescribe: String? = "") :
    Parcelable {

    constructor(parcel: Parcel) : this() {
        bookId = parcel.readInt()
        bookName = parcel.readString()
        bookDescribe = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bookId)
        parcel.writeString(bookName)
        parcel.writeString(bookDescribe)
    }

    fun readFromParcel(parcel: Parcel) {
        bookId = parcel.readInt()
        bookName = parcel.readString()
        bookDescribe = parcel.readString()
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Book(bookId=$bookId, bookName=$bookName, bookDescribe=$bookDescribe)"
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }

}