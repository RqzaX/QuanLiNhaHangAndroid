package com.example.nhahang

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChiTietMonFramgent : DialogFragment() {
    private lateinit var edtTenMon : EditText
    private lateinit var edtGiaMon : EditText
    private lateinit var edtGhiChu : EditText
    private lateinit var spDanhMuc : Spinner
    private lateinit var btnSua : Button
    private lateinit var btnXoa : Button
    private lateinit var imgMon : ImageView
    private lateinit var   selectedItem : MonAn
    private lateinit var db : AppDatabase
    private  lateinit var monAnDAO :MonAnDAO
    private lateinit var activityRef : QuanLiMonActivity

    private var imgMonInt : Int = 0
    override fun onStart(){
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
             selectedItem = arguments?.getParcelable("selectedMon")?:MonAn(0,"","",0,0,"")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.framgent_chi_tiet_mon, container, false)
    }
    override fun onViewCreated(view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setControl(view)

        setEvent()
    }


    //set control
    fun setControl(view :View){
         edtTenMon = view.findViewById(R.id.edtTenMon)
        edtGiaMon = view.findViewById(R.id.edtGiaMon)
        edtGhiChu = view.findViewById(R.id.edtGhiChu)
        spDanhMuc = view.findViewById(R.id.spDanhMucCT)
            imgMon = view.findViewById(R.id.imgMon)
    btnSua = view.findViewById(R.id.btnXacNhanSua)
    btnXoa = view.findViewById(R.id.btnXoaMon)
    imgMon.setBackgroundResource(imgMonInt)
    }
    //set event
    fun setEvent(){
        var danhMuc = selectedItem.danhMuc
        var adapterSP = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,
            listOf("Món ăn","Lunch","Dinner","Breakfast","Healthy","Desert","Drink"))
        spDanhMuc.adapter = adapterSP
        for(i in 0 until adapterSP.count){
            if(adapterSP.getItem(i) == danhMuc){
                spDanhMuc.setSelection(i)
            }
        }
        spDanhMuc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                danhMuc = parent.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,"nhahang_db"
        ).build()
        monAnDAO = db.monAnDAO()
        edtTenMon.setText(selectedItem.tenMon)
        edtGiaMon.setText(selectedItem.giaMon.toString())
        edtGhiChu.setText(selectedItem.moTa)
        imgMon.setImageResource(selectedItem.anh)

        btnXoa.setOnClickListener {
            if(selectedItem != null){

                GlobalScope.launch(Dispatchers.IO) {
                    monAnDAO.deleteMonAn(selectedItem)
                    withContext(Dispatchers.Main){
                        activityRef?.loadMon("")
                        Toast.makeText(requireContext(),"Xóa món thành công",Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
            }
        }
        btnSua.setOnClickListener {
            if(selectedItem != null){
                var tenMon =edtTenMon.text.toString()
                var giaMon = edtGiaMon.text.toString()?.toIntOrNull()
                var ghiChu = edtGhiChu.text.toString()
                var anh = R.drawable.brand_108fmk
                if(tenMon != "" && giaMon != null && ghiChu != ""){
                  var updateMon = selectedItem.copy(tenMon = tenMon, giaMon =  giaMon, moTa = ghiChu, danhMuc = danhMuc)
                    GlobalScope.launch(Dispatchers.IO) {
                        monAnDAO.updateMonAn(updateMon)
                        withContext(Dispatchers.Main){
                            activityRef?.loadMon("")
                            Toast.makeText(requireContext(),"cập nhật món thành công",Toast.LENGTH_SHORT).show()
                            dismiss()
                        }
                    }
                }
            }
        }

    }
        fun setActivity(activity: QuanLiMonActivity) {
            this.activityRef = activity
        }

}