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

class ThongKeMonAnActivity : AppCompatActivity() {

    private lateinit var btnQuayLai : Button
    private lateinit var tvTMTN : TextView
    private lateinit var db : AppDatabase
    private lateinit var chiTietHoaDonDAO : ChiTietHoaDonDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_thong_ke_mon_an)
        setControl()
        setEvent()
    }
    //set control
    fun setControl(){
        btnQuayLai = findViewById(R.id.btnQuayLai3)
        tvTMTN = findViewById(R.id.tvTMTN)
    }
    //set event
    fun setEvent(){
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        chiTietHoaDonDAO = db.cthdDAO()
        GlobalScope.launch(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            // Ngày hiện tại
            val ngayHomNay = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
            var tongMon = chiTietHoaDonDAO.getTongSoLuongTrongNgay(ngayHomNay)
            withContext(Dispatchers.Main) {
                tvTMTN.text = tongMon.toString() + " món"
            }
        }
        btnQuayLai.setOnClickListener{
            finish()
        }
    }
}