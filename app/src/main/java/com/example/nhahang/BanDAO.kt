package com.example.nhahang

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BanDAO {
    @Insert
    fun InsertBan(ban: Ban)

    @Update
    fun UpdateBan(ban: Ban)

    @Delete
    fun DeleteBan(ban: Ban)

    @Query("SELECT * FROM Ban")
    fun getAllBan(): List<Ban>

    @Query("SELECT * FROM Ban where maBan = :maBan")
    fun getAllBanByID(maBan : Int): Ban


    @Query("SELECT * FROM Ban where tang = 1")
    fun getAllBanTang1(): List<Ban>

    @Query("SELECT * FROM Ban where tang = 2")
    fun getAllBanTang2(): List<Ban>
}