package com.example.nhahang

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThemMonFramgent : DialogFragment() {
    private lateinit var edtTenMon : EditText
    private lateinit var edtGiaMon : EditText
    private lateinit var edtGhiChu : EditText
    private lateinit var spDanhMuc : Spinner
    private lateinit var btnThem : Button
    private lateinit var db : AppDatabase
    private lateinit var monAnDAO: MonAnDAO
    private  var activityRef: QuanLiMonActivity? = null

    override fun onStart(){
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.framgent_them_mon, container, false)
    }
    override fun onViewCreated(view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setControl(view)
        setEvent()

    }
    //set control
    fun setControl(view: View){
        btnThem = view.findViewById(R.id.btnThemMonAn)
        edtTenMon = view.findViewById(R.id.edtTenMon)
        edtGiaMon = view.findViewById(R.id.edtGiaMon)
        edtGhiChu = view.findViewById(R.id.edtGhiChu)
        spDanhMuc = view.findViewById(R.id.spDanhMuc)

    }
    //set event
    fun setEvent(){
        db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        monAnDAO = db.monAnDAO()
        spDanhMuc.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,
            listOf("Món ăn","Lunch","Dinner","Breakfast","Healthy","Desert","Drink"))
        var danhMuc = spDanhMuc.getItemAtPosition(0).toString()
        spDanhMuc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                danhMuc = parent.getItemAtPosition(position) as String
                Toast.makeText(requireContext(), "Selected: $danhMuc", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        btnThem.setOnClickListener {
            val tenMon = edtTenMon.text.toString()?:""
            val giaMon = edtGiaMon.text.toString()?.toIntOrNull()?:0
            val ghiChu = edtGhiChu.text.toString()?:""
            val anh = R.drawable.brand_108fmk
                if(tenMon != "" && giaMon != 0 && ghiChu != ""){
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        monAnDAO.InsertMonAn(monAn = MonAn(tenMon = tenMon, giaMon = giaMon, moTa = ghiChu, anh = anh, danhMuc = danhMuc))
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Thêm món thành công,với danh mục : ${danhMuc}", Toast.LENGTH_SHORT).show()
                            Log.d("ThemMon", "Them mon thanh cong: $tenMon, $giaMon, $danhMuc") // Thêm dòng log
                            activityRef?.loadMon("Category")
                            dismiss()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Lỗi khi thêm món: ${e.message}", Toast.LENGTH_SHORT).show()
                            Log.e("ThemMon", "Loi khi them mon: ${e.message}")
                        }
                    }
                }
            }else{
                Toast.makeText(requireContext(),"Thêm món không thành công",Toast.LENGTH_SHORT).show()

            }
        }
    }
    fun setActivity(activity: QuanLiMonActivity) {
        this.activityRef = activity
    }
    //add mon

}