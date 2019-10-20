package com.admins.dq.model.oldModel

import android.os.Parcel
import android.os.Parcelable
import com.admins.dq.drawerui.home.adapter.DynamicSearchAdapter

data class Patient(val name: String) : DynamicSearchAdapter.Searchable, Parcelable {
    override fun getSearchCriteria(): String {
        return this.name
    }

    constructor(parcel: Parcel) : this(parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Patient> {
        override fun createFromParcel(parcel: Parcel): Patient {
            return Patient(parcel)
        }

        override fun newArray(size: Int): Array<Patient?> {
            return arrayOfNulls(size)
        }
    }
}