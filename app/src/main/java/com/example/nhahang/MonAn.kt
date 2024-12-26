package com.example.nhahang

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "MonAn")
data class MonAn(
     @PrimaryKey(autoGenerate = true) var maMon : Int = 0,
    var tenMon : String, var moTa : String, var giaMon : Int ,  var anh : Int, var danhMuc : String):
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(maMon) // Ghi maMon vào Parcel
        parcel.writeString(tenMon)
        parcel.writeString(moTa)
        parcel.writeInt(giaMon)
        parcel.writeInt(anh)
        parcel.writeString(danhMuc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<MonAn> {
            override fun createFromParcel(parcel: Parcel): MonAn {
                return MonAn(parcel)
            }

            override fun newArray(size: Int): Array<MonAn?> {
                return arrayOfNulls(size)
            }
        }
    }
    }