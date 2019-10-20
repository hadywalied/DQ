package com.admins.dq.model


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class BaseComplaints(
    @field:Json(name = "Table")
    val table: List<Table?>? = listOf()
) : Parcelable {


    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Table)) {
    }

    data class Table(
        @field:Json(name = "BaseComplainD")
        val baseComplainD: Int? = 0,
        @field:Json(name = "BaseComplainName")
        val baseComplainName: String? = ""
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(baseComplainD)
            parcel.writeString(baseComplainName)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Table> {
            override fun createFromParcel(parcel: Parcel): Table {
                return Table(parcel)
            }

            override fun newArray(size: Int): Array<Table?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(table)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseComplaints> {
        override fun createFromParcel(parcel: Parcel): BaseComplaints {
            return BaseComplaints(parcel)
        }

        override fun newArray(size: Int): Array<BaseComplaints?> {
            return arrayOfNulls(size)
        }
    }

}
