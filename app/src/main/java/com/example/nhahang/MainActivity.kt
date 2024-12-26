package com.example.nhahang

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    //khai bao thuoc tinh
    private lateinit var viewPage : ViewPager2
    private var recyclerItems : MutableList<RecyclerItem> = mutableListOf()
    private lateinit var recyclerView : RecyclerView
    private lateinit var recyclerTopProducts : RecyclerView
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var edtTimKiem : SearchView
    private lateinit var adapter : RecyclerTopProductsAdapter
    private lateinit var db : AppDatabase
    private lateinit var monAnDAO : MonAnDAO
    private lateinit var chiTietHoaDonDAO : ChiTietHoaDonDAO
    private lateinit var hoaDonDAO : HoaDonDAO
    private lateinit var banDAO : BanDAO

    private lateinit var items :List<MonAn>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setControl()
        createDataBase()
        doDuLieuVaoViewPage()
        //goi ham load list recycler view(danh mục sản phẩm)
        doDuLieuVaoRecyler()
        //goi ham load (top sản phẩm)
        doDuLieuVaoRecylerView()
        //goi ham setevent bottom nav
        setEventNav()
        event()
    }
    //create database
    fun createDataBase(){
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        monAnDAO = db.monAnDAO()
        chiTietHoaDonDAO = db.cthdDAO()
        hoaDonDAO = db.hoaDonDAO()
        banDAO = db.banDAO()
        //chen du lieu vao danh sach mon an
//        GlobalScope.launch(Dispatchers.IO) {
//            val monAn1 = MonAn(tenMon = "Tôm sốt thái",moTa = "Tôm tươi được chế biến với sốt Thái cay cay, chua ngọt đặc trưng, đậm đà hương vị Đông Nam Á.",giaMon = 129000,anh = R.drawable.img_top_product1,danhMuc = "Món ăn")
//            val monAn2 = MonAn(tenMon = "Chân gà xả tắc",moTa = "Chân gà dai giòn, ngâm cùng sả thơm, tắc chua và gia vị cay nồng, tạo nên món ăn vặt hấp dẫn",giaMon = 119000,anh = R.drawable.img_top_product2,danhMuc = "Món ăn")
//            val monAn3 = MonAn(tenMon = "Tôm sốt thái",moTa = "Tôm tươi được tẩm sốt Thái đậm vị, kết hợp cùng các loại rau thơm và gia vị chua cay hoàn hảo",giaMon = 129000,anh = R.drawable.img_top_product3,danhMuc = "Món ăn")
//            val monAn4 = MonAn(tenMon = "Hàu nướng phô mai",moTa = "Hàu tươi nướng béo ngậy, phủ lớp phô mai tan chảy thơm lừng, là món ăn ngon khó cưỡng",giaMon = 69000,anh = R.drawable.img_top_product4,danhMuc = "Món ăn")
//            val monAn5 = MonAn(tenMon = "Pessi",moTa = "",giaMon = 20000,anh = R.drawable.img_4,danhMuc = "Nước")
//            val monAn6 = MonAn(tenMon = "Coca cola",moTa = "",giaMon = 20000,anh = R.drawable.img_5,danhMuc = "Nước")
//            val monAn7 = MonAn(tenMon = "Beer",moTa = "",giaMon = 25000,anh = R.drawable.img_6,danhMuc = "Nước")
//            val monAn8 = MonAn(tenMon = "Beer",moTa = "",giaMon = 20000,anh = R.drawable.img_7,danhMuc = "Nước")
//
//            monAnDAO.InsertMonAn(monAn1)
//            monAnDAO.InsertMonAn(monAn2)
//            monAnDAO.InsertMonAn(monAn3)
//            monAnDAO.InsertMonAn(monAn4)
//            monAnDAO.InsertMonAn(monAn5)
//            monAnDAO.InsertMonAn(monAn6)
//            monAnDAO.InsertMonAn(monAn7)
//            monAnDAO.InsertMonAn(monAn8)
//
//            banDAO.InsertBan(Ban(tenBan = "Bàn 1",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 2",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 3",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 4",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 5",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 6",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 8",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 9",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 10",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 11",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 12",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 13",tang = 1))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 14",tang = 1))
//
//            banDAO.InsertBan(Ban(tenBan = "Bàn 1",tang = 2))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 2",tang = 2))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 3",tang = 2))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 4",tang = 2))
//            banDAO.InsertBan(Ban(tenBan = "Bàn 5",tang = 2))
//
//
////chen du lieu vao chi tiet hoa don
//            val chiTietHoaDon1 = ChiTietHoaDon(maHoaDon = 1, maMon = 1, soLuong = 1, tienMoiMon = 10000, tongTienMon = 10000)
//            val chiTietHoaDon2 = ChiTietHoaDon(maHoaDon = 1, maMon = 2, soLuong = 3, tienMoiMon = 20000, tongTienMon = 60000)
//            val chiTietHoaDon3 = ChiTietHoaDon(maHoaDon = 2, maMon = 3, soLuong = 5, tienMoiMon = 30000, tongTienMon = 150000)
//            val chiTietHoaDon4 = ChiTietHoaDon(maHoaDon = 2, maMon = 4, soLuong = 3, tienMoiMon = 40000, tongTienMon = 120000)
//            val chiTietHoaDon5 = ChiTietHoaDon(maHoaDon = 3, maMon = 1, soLuong = 2, tienMoiMon = 10000, tongTienMon = 20000)
//            val chiTietHoaDon6 = ChiTietHoaDon(maHoaDon = 3, maMon = 4, soLuong = 4, tienMoiMon = 40000, tongTienMon = 160000)
////
////            chiTietHoaDonDAO.InsertChiTietHoaDon(chiTietHoaDon1)
////            chiTietHoaDonDAO.InsertChiTietHoaDon(chiTietHoaDon2)
////            chiTietHoaDonDAO.InsertChiTietHoaDon(chiTietHoaDon3)
////            chiTietHoaDonDAO.InsertChiTietHoaDon(chiTietHoaDon4)
////            chiTietHoaDonDAO.InsertChiTietHoaDon(chiTietHoaDon5)
////            chiTietHoaDonDAO.InsertChiTietHoaDon(chiTietHoaDon6)
//
////            val hoadon1 = HoaDon( Ma = "Bàn 1", thoiGianDat = "25/12/2024", trangThai = "Đã thanh toán", tongTien = 70000)
////            val hoadon2 = HoaDon(tenBan = "Bàn 2", thoiGianDat = "25/12/2024", trangThai = "Đã thanh toán", tongTien = 270000)
////            val hoadon3 = HoaDon(tenBan = "Bàn 1", thoiGianDat = "25/1/2025", trangThai = "Đã thanh toán", tongTien = 180000)
////            val hoadon4 = HoaDon(tenBan = "Bàn 1", thoiGianDat = "29/12/2025", trangThai = "Đã thanh toán", tongTien = 180000)
//////            hoaDonDAO.InsertHoaDon(hoadon1)
////            hoaDonDAO.InsertHoaDon(hoadon2)
////            hoaDonDAO.InsertHoaDon(hoadon3)
////            hoaDonDAO.InsertHoaDon(hoadon4)
//        }
    }



    //set control
    fun setControl(){
        edtTimKiem = findViewById(R.id.searchView)
    }
    //do du lieu banner vao view page 2
    fun doDuLieuVaoViewPage(){
        viewPage = findViewById(R.id.vpBanner)
        var items = mutableListOf<Int>()
        items.add(R.drawable.img_lv_banner3)
        items.add(R.drawable.img_lv_banner1)
        val adapter = ViewPageAdapter(this,items)
        viewPage.adapter = adapter
    }
    //do du lieu recycler adapter vao recycler
    fun doDuLieuVaoRecyler(){
        recyclerView = findViewById(R.id.reyclerView)
        //add list recycler
        recyclerItems.add(RecyclerItem(R.drawable.img_topnav_all,"All"))
        recyclerItems.add(RecyclerItem(R.drawable.img_topnav_breakfastt,"Drink"))
        recyclerItems.add(RecyclerItem(R.drawable.img_topnav_lunch,"Lunch"))
        recyclerItems.add(RecyclerItem(R.drawable.img_topnav_dinner,"Dinner"))
        recyclerItems.add(RecyclerItem(R.drawable.img_topnav_healthy,"Healthy"))
        recyclerItems.add(RecyclerItem(R.drawable.img_topnav_desert,"Desert"))
        //khai bao adapter
        val adapterRecycler = RecyclerAdapter(this, recyclerItems) { position, item ->
            Toast.makeText(this,"Ban chon ${item.title}",Toast.LENGTH_SHORT).show()
            loadTopSanPham(item.title)



        }
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapterRecycler
    }


    fun doDuLieuVaoRecylerView(){
        recyclerTopProducts = findViewById(R.id.recyclerTopProducts)
         items = listOf()
         adapter = RecyclerTopProductsAdapter(this,items)
        loadTopSanPham("All")
        recyclerTopProducts.layoutManager = GridLayoutManager(this,2)
        recyclerTopProducts.adapter = adapter
        edtTimKiem.setOnQueryTextListener( object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList = items.filter { item->
                    item.tenMon.contains(newText.toString(),ignoreCase = true)
                }
                adapter.updateData(newList)
                return true
            }

        })
        //        {
//            val searchText = it?.toString() ?: ""
//            // Lọc danh sách theo nội dung tìm kiếm
//            val filteredList = items.filter { item ->
//                item.tenMon.contains(searchText, ignoreCase = true)
//            }
//             //Cập nhật danh sách trong adapter
//            adapter.updateData(filteredList)
//        }
    }

    fun loadTopSanPham(danhMuc : String){
        GlobalScope.launch(Dispatchers.IO) {
            if(danhMuc == "All"){

                items =  monAnDAO.getAllMons()
            }else{
                items =  monAnDAO.getMonAnByDanhMuc(danhMuc)

            }
            withContext(Dispatchers.Main){
                adapter.updateData(items)
            }
        }
    }
    //thuc hien event
    fun setEventNav(){
        bottomNav = findViewById(R.id.bottom_nav_view)
        bottomNav.selectedItemId = R.id.bottom_nav_home
        bottomNav.setOnNavigationItemSelectedListener { i ->
            when(i.itemId){
                R.id.bottom_nav_home->{
                    startActivity(Intent(this,MainActivity::class.java))
                    overridePendingTransition(R.anim.animation_activity, R.anim.animation_activity)
                    true
                }
                R.id.bottom_nav_chart->{
                    if(taikhoan.quyen == "admin") {
                        startActivity(Intent(this,ThongKeActivity::class.java))
                        overridePendingTransition(R.anim.animation_activity, R.anim.animation_activity)
                        true
                    }else{
                        false
                    }
                }
                R.id.bottom_nav_location->{
                    startActivity(Intent(this,KhuVucActivity::class.java))
                    overridePendingTransition(R.anim.animation_activity, R.anim.animation_activity)
                    true
                }
                R.id.bottom_nav_work->{
                    if(taikhoan.quyen == "admin") {
                    startActivity(Intent(this,QuanLiActivity::class.java))
                    overridePendingTransition(R.anim.animation_activity, R.anim.animation_activity)
                        true
                    }else{
                        false
                    }
                }
                R.id.bottom_nav_extension->{
                    startActivity(Intent(this,TienIchActivity::class.java))
                    overridePendingTransition(R.anim.animation_activity, R.anim.animation_activity)
                    true
                }else -> false
            }
        }

    }
    //set event
    fun event(){
//        setSupportActionBar(tbMain)
//        supportActionBar?.title = "Home"
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}