package com.example.nhahang

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ChiTietHoaDon")
data class ChiTietHoaDon(
    @PrimaryKey(autoGenerate = true) val maChiTietHoaDon: Int = 0,
    val maHoaDon: Int,
    val maMon: Int,
    val soLuong: Int,
    val tienMoiMon: Int,
    val tongTienMon: Int
)