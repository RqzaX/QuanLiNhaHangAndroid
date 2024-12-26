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
import androidx.core.content.ContextCompat
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GoiMonActivity : AppCompatActivity() {
    private lateinit var tv_TableName : TextView
    private lateinit var btnMon : Button
    private lateinit var btnNuoc : Button
    private lateinit var btnCart : Button
    private lateinit var btn_AddMon : Button

    private lateinit var lv : ListView
    private lateinit var itemsMonAn : MutableList<MonAn>
    private lateinit var itemsNuoc : MutableList<MonAn>
    private lateinit var monAnDAO :MonAnDAO
    private lateinit var hoaDonDAO :HoaDonDAO
    private lateinit var cthdDAO :ChiTietHoaDonDAO
    private lateinit var banDAO :BanDAO
    private lateinit var adapter : ListViewMonAdapter
    private lateinit var db : AppDatabase
    private lateinit var tv_priceMon : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_goi_mon)
       setControll()
        setEventt()
    }

    //set controll
    fun setControll(){
        lv = findViewById(R.id.lv_GoiMon)
        itemsMonAn = mutableListOf()
        itemsNuoc = mutableListOf()
        btnCart = findViewById(R.id.btnCart)
        btnMon = findViewById(R.id.btnMon)
        btnNuoc = findViewById(R.id.btnNuoc)
        tv_TableName = findViewById(R.id.tv_TableName)
        btn_AddMon = findViewById(R.id.btn_AddMon)
        tv_priceMon = findViewById(R.id.tv_priceMon)
    }
    //set eventt
    fun setEventt(){
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        monAnDAO = db.monAnDAO()
        hoaDonDAO = db.hoaDonDAO()
        cthdDAO = db.cthdDAO()
        banDAO = db.banDAO()
         adapter = ListViewMonAdapter(this,itemsMonAn)
        lv.adapter = adapter
        load()
        tv_TableName.text = intent.getStringExtra("TableName")
        btnCart.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val donHang = hoaDonDAO.getDonHangByTenBan(intent.getStringExtra("MaBan").toString().toInt())
                val intent = Intent(this@GoiMonActivity,XacNhanThemMonActivity::class.java)
                withContext(Dispatchers.Main){

                         intent.putExtra("maHD",donHang.maHoaDon.toString()?:"0")

                    intent.putExtra("TableName", tv_TableName.text.toString())
                    startActivity(intent)
                }
            }


        }
        btnMon.backgroundTintList = ContextCompat.getColorStateList(this,R.color.primary_color)
        btnMon.setOnClickListener {
            load("Món ăn")
            btnMon.backgroundTintList = ContextCompat.getColorStateList(this,R.color.primary_color)
            btnNuoc.backgroundTintList = ContextCompat.getColorStateList(this,R.color.transparent)
        }
        btnNuoc.setOnClickListener {
            load("Drink")
            btnNuoc.backgroundTintList = ContextCompat.getColorStateList(this,R.color.primary_color)
            btnMon.backgroundTintList = ContextCompat.getColorStateList(this,R.color.transparent)
        }
        btn_AddMon.setOnClickListener {
             GlobalScope.launch(Dispatchers.IO) {
                var hoaDon = hoaDonDAO.getDonHangByTenBan(intent.getStringExtra("MaBan").toString().toInt())
                withContext(Dispatchers.Main){

                    val monAnCurrent = btn_AddMon.getTag() as MonAn
                    if(hoaDon != null){
                        Toast.makeText(this@GoiMonActivity," co don hàng",Toast.LENGTH_SHORT).show()
                        GlobalScope.launch(Dispatchers.IO) {
                            val cthd : ChiTietHoaDon? = cthdDAO.getCTHDByMaHDMonAn(hoaDon.maHoaDon, monAnCurrent.maMon)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@GoiMonActivity,
                                    cthd.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if(cthd == null){
                                val soLuong = 1
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@GoiMonActivity,
                                        monAnCurrent.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                val donGia = monAnCurrent.giaMon
                                val chiTietHoaDon = ChiTietHoaDon(maHoaDon = hoaDon.maHoaDon, maMon = monAnCurrent.maMon, soLuong = soLuong, tienMoiMon = donGia, tongTienMon = soLuong * donGia)
                                cthdDAO.InsertChiTietHoaDon(chiTietHoaDon)
                                val hoaDonTongTien = hoaDon.copy(tongTien = (hoaDon.tongTien + chiTietHoaDon.tongTienMon))
                                hoaDonDAO.updateHoaDon(hoaDonTongTien)
                            }else{
                                val soLuong = cthd.soLuong + 1
                                val donGia = monAnCurrent.giaMon
                                val chiTietHoaDon = cthd.copy(maHoaDon = hoaDon.maHoaDon, maMon = monAnCurrent.maMon, soLuong = soLuong, tienMoiMon = donGia, tongTienMon = soLuong * donGia)
                                cthdDAO.UpdateChiTietHoaDon(chiTietHoaDon)
                                val hoaDonTongTien = hoaDon.copy(tongTien = (hoaDon.tongTien + chiTietHoaDon.tongTienMon))
                                hoaDonDAO.updateHoaDon(hoaDonTongTien)
                            }
                        }
                    }
                    else{
                        Toast.makeText(this@GoiMonActivity,"chua co don hàng",Toast.LENGTH_SHORT).show()
                        val ngayHomNay : String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                            Date()
                        )
                        Toast.makeText(this@GoiMonActivity,intent.getStringExtra("MaBan").toString(),Toast.LENGTH_SHORT).show()
                         hoaDon = HoaDon(maBan = intent.getStringExtra("MaBan").toString().toInt(), thoiGianDat = ngayHomNay, trangThai = "chưa thanh toán", tongTien = 0)
                        val banUpdate = Ban(maBan = intent.getStringExtra("MaBan").toString().toInt(), tenBan = intent.getStringExtra("TableName").toString(), trangThai = 1, tang = intent.getStringExtra("Tang").toString().toInt())
                        GlobalScope.launch(Dispatchers.IO) {
                            banDAO.UpdateBan(banUpdate)
                            hoaDonDAO.InsertHoaDon(hoaDon)
                            hoaDon = hoaDonDAO.getNewHoaDon()
                            val soLuong = 1
                            val donGia = monAnCurrent.giaMon
                            val chiTietHoaDon = ChiTietHoaDon(maHoaDon = hoaDon.maHoaDon, maMon = monAnCurrent.maMon, soLuong = soLuong, tienMoiMon = donGia, tongTienMon = soLuong * donGia)
                            cthdDAO.InsertChiTietHoaDon(chiTietHoaDon)
                            val hoaDonTongTien = hoaDon.copy(tongTien = (hoaDon.tongTien + chiTietHoaDon.tongTienMon))
                            hoaDonDAO.updateHoaDon(hoaDonTongTien)
                        }
                    }

                }
            }
        }
    }
    fun load(type : String = "Món ăn"){
            GlobalScope.launch(Dispatchers.IO) {
                var items : List<MonAn> = listOf()
                if(type == "Món ăn"){
                    items =  monAnDAO.getAllMonAns()
                }else if(type == "Drink"){
                    items =  monAnDAO.getMonAnByDanhMuc("Drink")
                }else{
                    items = monAnDAO.getAllMons()
                }

                withContext(Dispatchers.Main){
                    adapter.updateData(items)
                }
            }
        }
}