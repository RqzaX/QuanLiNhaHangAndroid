package com.example.nhahang

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QRThanhToanActivity : AppCompatActivity() {
    private lateinit var btnSubmit : Button
    private lateinit var hoaDonDAO : HoaDonDAO
    private lateinit var banDAO : BanDAO
    private lateinit var db : AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_qrthanh_toan)
       setControl()
        setEvent()
    }
    fun setControl(){
        btnSubmit = findViewById(R.id.btnSubmit)
    }
    fun setEvent(){
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        hoaDonDAO = db.hoaDonDAO()
        banDAO = db.banDAO()
        btnSubmit.setOnClickListener {
            startActivity(Intent(this,BillActivity::class.java))
            GlobalScope.launch(Dispatchers.IO) {
                val maBan = intent.getStringExtra("MaBan").toString().toInt()
                val donHang = hoaDonDAO.getDonHangByTenBan(maBan)
                val donHangThanhToan = donHang.copy(trangThai = "Đã thanh toán")
                hoaDonDAO.updateHoaDon(donHangThanhToan)
                val ban = banDAO.getAllBanByID(maBan)
                val banUpdate = ban.copy(trangThai = 0)
                banDAO.UpdateBan(banUpdate)
            }

        }

    }
}