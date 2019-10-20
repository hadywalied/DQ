package com.admins.dq.model


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class ResultsModel(
    @field:Json(name = "ask_id")
    val askId: Int? = 0,
    @field:Json(name = "Illness_Name")
    val illnessName: String? = "",
    @field:Json(name = "counting")
    val counting: Int? = 0,
    @field:Json(name = "FRRQ")
    val fRRQ: Double? = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(askId)
        parcel.writeString(illnessName)
        parcel.writeValue(counting)
        parcel.writeValue(fRRQ)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultsModel> {
        override fun createFromParcel(parcel: Parcel): ResultsModel {
            return ResultsModel(parcel)
        }

        override fun newArray(size: Int): Array<ResultsModel?> {
            return arrayOfNulls(size)
        }
    }
}
