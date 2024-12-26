package com.example.nhahang

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "HoaDon")
data class HoaDon(
    @PrimaryKey(autoGenerate = true) var maHoaDon : Int = 0,
    val maBan: Int,
    val thoiGianDat: String,
    val trangThai: String,
    val tongTien: Int)
