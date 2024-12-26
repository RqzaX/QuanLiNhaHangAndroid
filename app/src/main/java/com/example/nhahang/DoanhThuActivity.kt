package com.example.nhahang

import android.content.Intent
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

class DoanhThuActivity : AppCompatActivity() {
    private lateinit var btnQuayLai : Button
    private lateinit var db : AppDatabase
    private lateinit var tvDTNgay : TextView
    private lateinit var tvDTThang : TextView

    private lateinit var hoaDonDAO : HoaDonDAO
    private lateinit var chiTietHoaDonDAO : ChiTietHoaDonDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doanh_thu)
        setControl()
        setEvent()
    }
    fun setControl(){
        btnQuayLai = findViewById(R.id.btnQuayLai)
        tvDTNgay = findViewById(R.id.tvDTNgay)
        tvDTThang = findViewById(R.id.tvDTThang)

    }
    fun setEvent(){
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        hoaDonDAO = db.hoaDonDAO()
        chiTietHoaDonDAO = db.cthdDAO()
        btnQuayLai.setOnClickListener{
            finish()
        }
        getDoanhThu()
    }
    //get doanh thu
    fun getDoanhThu(){
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        // Ngày hiện tại
        val ngayHomNay = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)

        // Ngày đầu tháng
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val dauThang = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)

        // Ngày cuối tháng
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val cuoiThang = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)

        GlobalScope.launch(Dispatchers.IO) {
            val doanhThuTrongNgay  = hoaDonDAO.getDoanhThuTrongNgay(ngayHomNay).toString()
            val doanhThuTrongThang  = hoaDonDAO.getDoanhThuTrongThang(dauThang,cuoiThang).toString()
            withContext(Dispatchers.Main) {
                val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                tvDTNgay.text = format.format(doanhThuTrongNgay.toDouble())
                tvDTThang.text = format.format(doanhThuTrongThang.toDouble())
            }
        }
    }

}