package com.admins.dq.model


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class AnswersModel(
    @field:Json(name = "answers")
    val answers: List<String?>? = listOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createStringArrayList()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(answers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AnswersModel> {
        override fun createFromParcel(parcel: Parcel): AnswersModel {
            return AnswersModel(parcel)
        }

        override fun newArray(size: Int): Array<AnswersModel?> {
            return arrayOfNulls(size)
        }
    }
}
