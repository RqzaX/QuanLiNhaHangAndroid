package com.example.nhahang

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import java.text.NumberFormat
import java.util.Locale

class XacNhanThemMonActivity : AppCompatActivity() {
    private lateinit var btnSubmit : Button
    private lateinit var tvTableName : TextView
    private lateinit var lv_ThemMon : ListView
    private lateinit var tvTongTien : TextView
    private  var ListBillInfo : List<BillInfo> = mutableListOf()
    private lateinit var db : AppDatabase
    private lateinit var chiTietHoaDonDAO : ChiTietHoaDonDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_xac_nhan_goi_mon)
        setControl()
        setEvent()
    }
    //set control
    fun setControl(){
        btnSubmit = findViewById(R.id.btnSubmit)
        tvTableName = findViewById(R.id.tv_TableName)
        lv_ThemMon = findViewById(R.id.lv_ThemMon)
        tvTongTien = findViewById(R.id.tvTongtien)
    }
    fun setEvent(){
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        chiTietHoaDonDAO = db.cthdDAO()
        val mahd = intent.getStringExtra("maHD")?.toInt()
        Toast.makeText(this,mahd.toString(),Toast.LENGTH_SHORT).show()
        GlobalScope.launch(Dispatchers.IO) {
            ListBillInfo = chiTietHoaDonDAO.getCTHD(mahd!!)
            Log.d("ListBillInfo",ListBillInfo.joinToString())
            val adapter = DatMonAdapter(this@XacNhanThemMonActivity,ListBillInfo,tvTongTien)
            lv_ThemMon.adapter = adapter
            if(ListBillInfo.size > 0){

                loadTongTien(ListBillInfo);
            }
        }
        tvTableName.text = intent.getStringExtra("TableName")
        btnSubmit.setOnClickListener {
            startActivity(Intent(this,KhuVucActivity::class.java))

        }



    }
    fun loadTongTien(ListBillInfo : List<BillInfo>){
        var tongTien  = 0.0
        ListBillInfo.forEach{item ->
            tongTien += item.price
        }
        val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        tvTongTien.text = "Tổng tiền: ${format.format(tongTien)}"
    }

}