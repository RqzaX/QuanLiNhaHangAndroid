package com.example.nhahang

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuanLiMonActivity : AppCompatActivity() {
    private lateinit var btnMon : Button
    private lateinit var btnNuoc : Button
    private lateinit var btnThemMon: Button
    private lateinit var lvQLMon : ListView
    private lateinit var listMon : List<MonAn>
    private lateinit var db :AppDatabase
    private lateinit var adapter :ListViewAdapter

    private lateinit var monAnDAO: MonAnDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quan_li_mon)
        setControl()
        setEvent()
    }
    //set control
    fun setControl() {
        btnMon = findViewById(R.id.btnMon)
        btnNuoc = findViewById(R.id.btnNuoc)
        btnThemMon = findViewById(R.id.btnThemMon)
        lvQLMon = findViewById(R.id.lvQLMon)
    }
    //set event
    fun setEvent(){

        btnThemMon.setOnClickListener {
            val fang  = ThemMonFramgent()
            fang.setActivity(this)
            fang.show(supportFragmentManager,"add form dia log")
        }
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        monAnDAO = db.monAnDAO()
        btnMon.backgroundTintList = ContextCompat.getColorStateList(this,R.color.primary_color)
        btnMon.setOnClickListener {
            loadMon("NoDrink")
            btnMon.backgroundTintList = ContextCompat.getColorStateList(this,R.color.primary_color)
            btnNuoc.backgroundTintList = ContextCompat.getColorStateList(this,R.color.transparent)
        }
        btnNuoc.setOnClickListener {
            loadMon("Drink")
            btnNuoc.backgroundTintList = ContextCompat.getColorStateList(this,R.color.primary_color)
            btnMon.backgroundTintList = ContextCompat.getColorStateList(this,R.color.transparent)
        }
        adapter = ListViewAdapter(this, listOf())
        lvQLMon.adapter = adapter
        loadMon("NoDrink")
        lvQLMon.setOnItemClickListener { parent, view, position, id ->
            // Kiểm tra đối tượng đã chọn
            val selectedItem = parent.getItemAtPosition(position) as MonAn
            Toast.makeText(this, "Bạn đã chọn món: ${selectedItem.tenMon}", Toast.LENGTH_SHORT).show()
            val fang = ChiTietMonFramgent()
            fang.setActivity(this)
            val bundle = Bundle()
            bundle.putParcelable("selectedMon", selectedItem)
            fang.arguments = bundle
            fang.show(supportFragmentManager, "add form dia log")
        }


    }
    //load mon an
    fun loadMon(type : String ){
        GlobalScope.launch(Dispatchers.IO) {
            if(type == "Drink"){
                listMon = monAnDAO.getMonAnByDanhMuc("Drink")
            }else{
                listMon = monAnDAO.getAllMonAns()
            }
                withContext(Dispatchers.Main){
                    adapter.updateData(listMon)
                    Log.d("QuanLiMonActivity", "Dữ liệu đã được load: ${listMon.size} món")
                }
        }

        btnMon.backgroundTintList = ContextCompat.getColorStateList(this,R.color.primary_color)
        btnNuoc.backgroundTintList = ContextCompat.getColorStateList(this,R.color.transparent)

    }
    //
}