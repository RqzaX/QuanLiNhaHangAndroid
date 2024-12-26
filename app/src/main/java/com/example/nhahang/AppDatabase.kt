package com.example.nhahang

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MonAn::class,HoaDon::class,ChiTietHoaDon::class,Ban::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun monAnDAO():MonAnDAO
    abstract fun hoaDonDAO():HoaDonDAO
    abstract fun cthdDAO():ChiTietHoaDonDAO
    abstract fun banDAO():BanDAO
}