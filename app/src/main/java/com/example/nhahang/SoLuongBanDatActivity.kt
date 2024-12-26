package com.example.nhahang

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SoLuongBanDatActivity : AppCompatActivity() {
    private lateinit var btnQuayLai : Button
    private lateinit var tvBDHN : TextView
    private lateinit var db : AppDatabase
    private lateinit var hoaDonDAO : HoaDonDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_so_luong_ban_dat)
        setControl()
        setEvent()
    }
    //set control
    fun setControl(){
        btnQuayLai = findViewById(R.id.btnQuayLai2)
        tvBDHN = findViewById(R.id.tvBDHN)
    }
    //set event
    fun setEvent(){
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        hoaDonDAO = db.hoaDonDAO()
        btnQuayLai.setOnClickListener{
            finish()
        }
        GlobalScope.launch(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            // Ngày hiện tại
            val ngayHomNay = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)

            val soLuongBan  = hoaDonDAO.getAllHoaDonTrongNgay(ngayHomNay).toString()
            withContext(Dispatchers.Main) {
                tvBDHN.text = soLuongBan.toString() + " Bàn"
            }
        }
    }
}