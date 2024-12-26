package com.example.nhahang

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
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
import java.util.Locale

class GioHangActivity : AppCompatActivity() {
    private lateinit var btnThanhToan : Button
    private lateinit var btnThemMon : Button
    private lateinit var tvTableName : TextView
    private lateinit var btnQuayLai : Button
    private lateinit var lvGioHang : ListView
    private lateinit var tvTongtien : TextView
    private  var ListBillInfo : List<BillInfo> = mutableListOf()
    private lateinit var db : AppDatabase
    private lateinit var hoaDonDAO : HoaDonDAO
    private lateinit var chiTietHoaDonDAO : ChiTietHoaDonDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gio_hang)
        setControl()
        setEvent()
    }
    //set control
    fun setControl(){
        tvTableName = findViewById(R.id.tv_TableName)
        btnThanhToan = findViewById(R.id.btnThanhToan)
        btnThemMon = findViewById(R.id.btnThemMon)
        btnQuayLai = findViewById(R.id.btnQuayLai_GioHang)
        lvGioHang = findViewById(R.id.lvGioHang)
        tvTongtien = findViewById(R.id.tvTongHoaDon)
    }
    //set event
    fun setEvent(){
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        chiTietHoaDonDAO = db.cthdDAO()
        hoaDonDAO = db.hoaDonDAO()

        tvTableName.text = intent.getStringExtra("TableName")

        btnThemMon.setOnClickListener {
            val tang = intent.getStringExtra("Tang")
            val maBan = intent.getStringExtra("MaBan")
            val intent = Intent(this,GoiMonActivity::class.java)
            intent.putExtra("TableName",tvTableName.text)
            intent.putExtra("Tang",tang)
            intent.putExtra("MaBan",maBan)
            startActivity(intent)
        }
        btnQuayLai.setOnClickListener {
            finish()
        }
        GlobalScope.launch(Dispatchers.IO) {
            val donHang = hoaDonDAO.getDonHangByTenBan(intent.getStringExtra("MaBan").toString().toInt())
            ListBillInfo = chiTietHoaDonDAO.getCTHD(donHang.maHoaDon)
            withContext(Dispatchers.Main) {
                val adapter = GioHangListViewAdapte(this@GioHangActivity,ListBillInfo)
                lvGioHang.adapter = adapter
                var tongTien : Int = 0
                ListBillInfo.forEach{item ->
                    tongTien += item.price
                }
                val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                tvTongtien.text = "Tổng tiền: ${format.format(tongTien)}"

            }

        }
        btnThanhToan.setOnClickListener {
            val maBan = intent.getStringExtra("MaBan")
            val intent = Intent(this,QRThanhToanActivity::class.java)

            intent.putExtra("MaBan",maBan)
            startActivity(intent)
        }


   }
}