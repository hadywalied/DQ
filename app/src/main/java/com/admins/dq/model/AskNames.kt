package com.admins.dq.model


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class AskNames(
    @field:Json(name = "AskName")
    val askName: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(askName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AskNames> {
        override fun createFromParcel(parcel: Parcel): AskNames {
            return AskNames(parcel)
        }

        override fun newArray(size: Int): Array<AskNames?> {
            return arrayOfNulls(size)
        }
    }
}