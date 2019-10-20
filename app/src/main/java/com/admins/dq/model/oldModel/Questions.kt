package com.admins.dq.model.oldModel

import android.os.Parcel
import android.os.Parcelable

data class Questions(val question: String?, var answer: String? = "no") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeString(answer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Questions> {
        override fun createFromParcel(parcel: Parcel): Questions {
            return Questions(parcel)
        }

        override fun newArray(size: Int): Array<Questions?> {
            return arrayOfNulls(size)
        }
    }
}