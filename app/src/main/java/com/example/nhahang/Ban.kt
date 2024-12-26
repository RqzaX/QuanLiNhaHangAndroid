package com.example.nhahang

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ban")
data class Ban (
    @PrimaryKey(autoGenerate = true) val maBan : Int = 0,
    val tenBan : String,val trangThai : Int = 0,
    val tang : Int
)